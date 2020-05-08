package com.wkk.community.controller;

import com.wkk.community.annotation.LoginRequired;
import com.wkk.community.entity.Comment;
import com.wkk.community.entity.DiscussPost;
import com.wkk.community.entity.User;
import com.wkk.community.service.CommentService;
import com.wkk.community.service.DiscussPostService;
import com.wkk.community.service.LikeService;
import com.wkk.community.service.UserService;
import com.wkk.community.util.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.*;

/**
 * @Time: 2020/5/1上午11:27
 * @Author: kongwiki
 * @Email: kongwiki@163.com
 */
@Controller
@RequestMapping("/discuss")
public class DiscussPostController implements CommunityConstant {
    @Autowired
    private DiscussPostService discussPostService;
    @Autowired
    private UserService userService;
    @Autowired
    private CommentService commentService;
    @Autowired
    private LikeService likeService;
    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private HostHolder hostHolder;

    // 发布帖子
    @LoginRequired
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    @ResponseBody
    public String addDiscussPost(String title, String content){
        User user = hostHolder.getUser();
        if(user == null){
            return CommunityUtil.getJsonString(403, "亲，您还未登录");
        }
        DiscussPost discussPost = new DiscussPost();
        discussPost.setUserId(user.getId());
        discussPost.setTitle(title);
        discussPost.setContent(content);
        discussPost.setCreateTime(new Date());
        discussPostService.addDiscussPost(discussPost);

        // 计算帖子分数 存入缓存
        String postScoreKey = RedisKeyUtil.getPostScoreKey();
        redisTemplate.opsForSet().add(postScoreKey, discussPost.getId());

        // 报错的情况， 将来程序统一处理
        return CommunityUtil.getJsonString(0, "发布成功");


    }

    // 帖子详情页
    @RequestMapping(value = "/detail/{discussId}", method = RequestMethod.GET)
    public String getDetail(@PathVariable("discussId") int discussId, Page page, Model model){
        // 帖子内容
        DiscussPost discussPost = discussPostService.findDiscussPostById(discussId);
        model.addAttribute("post", discussPost);
        // 帖子获赞数量
        long postLikeCount = likeService.findEntityCount(discussPost.getId(), ENTITY_TYPE_POST);
        model.addAttribute("postLikeCount", postLikeCount);
        // 作者
        // 细节处理，通过userId 获取用户的头像或者名称
        // 后期使用redis进行优化(毕竟查询了两次表)
        User user = userService.findUserById(discussPost.getUserId());
        model.addAttribute("user", user);

        //评论的信息
        page.setLimit(5);
        page.setPath("/discuss/detail/" + discussId);
        page.setRows(discussPost.getCommentCount());
        List<Comment> comments = commentService.findCommentByEntity(ENTITY_TYPE_POST, discussPost.getId(), page.getOffset(), page.getLimit());
        // 包含：
        // 帖子评论
        // 用户评论
        List<Map<String, Object>> commentVoList = new ArrayList<>();
        if(comments != null){
            for (Comment comment : comments) {
                // 评论列表
                Map<String, Object> commentVo = new HashMap<>();
                // 评论
                commentVo.put("comment", comment);
                // 作者
                commentVo.put("user",  userService.findUserById(comment.getUserId()));
                // 评论获赞次数
                long commentLikeCount = likeService.findEntityCount(comment.getEntityId(), comment.getEntityType());
                commentVo.put("commentLikeCount", commentLikeCount);


                // 回复列表
                List<Comment> replyList = commentService.findCommentByEntity(ENTITY_TYPE_COMMENT, comment.getId(),
                        0, Integer.MAX_VALUE);

                // 回复vo列表
                List<Map<String, Object>> replyVoList = new ArrayList<>();
                if(replyList != null){
                    for (Comment reply : replyList) {
                        Map<String, Object> replyVo = new HashMap<>();
                        // 回复
                        replyVo.put("reply", reply);
                        // 作者
                        replyVo.put("user", userService.findUserById(reply.getUserId()));
                        // target
                        User target =  reply.getTargetId() == 0 ? null: userService.findUserById(reply.getTargetId());
                        replyVo.put("target", target);
                        // 回复获赞次数
                        long replyLikeCount = likeService.findEntityCount(reply.getEntityId(), reply.getEntityType());
                        replyVo.put("replyLikeCount", replyLikeCount);
                        replyVoList.add(replyVo);
                    }
                }
                commentVo.put("replys", replyVoList);
                // 回复数量
                int replyCount = commentService.findCountByEntity(ENTITY_TYPE_COMMENT, comment.getId());
                commentVo.put("replyCount", replyCount);

                commentVoList.add(commentVo);
            }
        }
        model.addAttribute("comments", commentVoList);

        return "site/discuss-detail";


    }

    // 置顶
    @RequestMapping(value = "/top", method = RequestMethod.POST)
    @ResponseBody
    public String setTop(int id){
        discussPostService.updateType(id, 1);
        return CommunityUtil.getJsonString(0);
    }

    // 加精
    @RequestMapping(value = "/wonderful", method = RequestMethod.POST)
    @ResponseBody
    public String setWonderful(int id){
        discussPostService.updateStatus(id, 1);

        String postScoreKey = RedisKeyUtil.getPostScoreKey();
        redisTemplate.opsForSet().add(postScoreKey, id);
        return CommunityUtil.getJsonString(0);
    }

    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    @ResponseBody
    public String setDelete(int id){
        discussPostService.updateStatus(id, 2);
        return CommunityUtil.getJsonString(0);
    }
}
