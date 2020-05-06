package com.wkk.community.controller;

import com.wkk.community.entity.Comment;
import com.wkk.community.entity.DiscussPost;
import com.wkk.community.entity.Event;
import com.wkk.community.event.EventProducer;
import com.wkk.community.service.CommentService;
import com.wkk.community.service.DiscussPostService;
import com.wkk.community.util.CommunityConstant;
import com.wkk.community.util.HostHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.Date;

/**
 * @Time: 2020/5/1下午8:00
 * @Author: kongwiki
 * @Email: kongwiki@163.com
 */
@Controller
@RequestMapping("/comment")
public class CommentController implements CommunityConstant {
    @Autowired
    private HostHolder hostHolder;
    @Autowired
    private CommentService commentService;
    @Autowired
    private DiscussPostService discussPostService;


    // 添加系统通知
    @Autowired
    private EventProducer eventProducer;

    @RequestMapping(value = "/add/{discussPostId}", method = RequestMethod.POST)
    public String addComment(@PathVariable("discussPostId") int discussPostId, Comment comment){
        comment.setUserId(hostHolder.getUser().getId());
        comment.setStatus(0);
        comment.setCreateTime(new Date());
        commentService.addComment(comment);
        // 触发评论事件
        Event event = new Event()
                .setTopic(TOPIC_COMMENT)
                .setUserId(hostHolder.getUser().getId())
                .setEntityId(comment.getEntityId())
                .setEntityType(comment.getEntityType())
                .setData("postId", discussPostId);
        // 事件发送的实体对应的Id (帖子ID|用户ID)
        if(comment.getEntityType() == ENTITY_TYPE_POST){
            DiscussPost target = discussPostService.findDiscussPostById(comment.getEntityId());
            event.setEntityUserId(target.getUserId());
        }else if(comment.getEntityType() == ENTITY_TYPE_COMMENT){
            Comment target = commentService.findCommentById(comment.getEntityId());
            event.setEntityUserId(target.getUserId());
        }
        eventProducer.fireEvent(event);
        return "redirect:/discuss/detail/" + discussPostId;
    }

}
