package com.wkk.community.controller;

import com.wkk.community.entity.Comment;
import com.wkk.community.entity.DiscussPost;
import com.wkk.community.entity.Event;
import com.wkk.community.entity.User;
import com.wkk.community.event.EventProducer;
import com.wkk.community.service.LikeService;
import com.wkk.community.util.CommunityConstant;
import com.wkk.community.util.CommunityUtil;
import com.wkk.community.util.HostHolder;
import com.wkk.community.util.RedisKeyUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.Map;

/**
 * @Time: 2020/5/4上午9:53
 * @Author: kongwiki
 * @Email: kongwiki@163.com
 */
@Controller
public class LikeController implements CommunityConstant {
    @Autowired
    private LikeService likeService;
    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private HostHolder hostHolder;

    // 添加系统通知
    @Autowired
    private EventProducer eventProducer;

    // ajax异步调用
    @RequestMapping(value = "/like", method = RequestMethod.POST)
    @ResponseBody
    public String like(int entityType, int entityId, int entityUserId, int postId){
        User user = hostHolder.getUser();
        // 点赞
        likeService.like(user.getId(), entityType, entityId, entityUserId);
        // 统计该帖子的点赞数量
        long likeCount = likeService.findEntityCount(entityId, entityType);
        // 状态
        int likeStatus = likeService.findEntityLikeStatus(user.getId(), entityId, entityType);
        Map<String, Object> map = new HashMap<>();
        map.put("likeCount", likeCount);
        map.put("likeStatus", likeStatus);

        // 触发点赞事件（单击: 赞， 双击: 取消赞）
        if(likeStatus == 1) {
            Event event = new Event()
                    .setTopic(TOPIC_LIKE)
                    .setUserId(hostHolder.getUser().getId())
                    .setEntityId(entityId)
                    .setEntityType(entityType)
                    .setEntityUserId(entityUserId)
                    .setData("postId", postId);
            eventProducer.fireEvent(event);
        }
        if(entityType == ENTITY_TYPE_POST){
            // 计算帖子分数 存入缓存
            String postScoreKey = RedisKeyUtil.getPostScoreKey();
            redisTemplate.opsForSet().add(postScoreKey, postId);

        }
        return CommunityUtil.getJsonString(0, null, map);
    }
}
