package com.wkk.community.controller;

import com.wkk.community.entity.User;
import com.wkk.community.service.LikeService;
import com.wkk.community.util.CommunityUtil;
import com.wkk.community.util.HostHolder;
import org.springframework.beans.factory.annotation.Autowired;
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
public class LikeController {
    @Autowired
    private LikeService likeService;
    @Autowired
    private HostHolder hostHolder;

    @RequestMapping(value = "/like", method = RequestMethod.POST)
    @ResponseBody
    public String like(int entityType, int entityId){
        User user = hostHolder.getUser();
        // 点赞
        likeService.like(user.getId(), entityType, entityId);
        // 统计该帖子的点赞数量
        long likeCount = likeService.findEntityCount(entityId, entityType);
        // 状态
        int likeStatus = likeService.findEntityLikeStatus(user.getId(), entityId, entityType);
        Map<String, Object> map = new HashMap<>();
        map.put("likeCount", likeCount);
        map.put("likeStatus", likeStatus);
        return CommunityUtil.getJsonString(0, null, map);
    }
}
