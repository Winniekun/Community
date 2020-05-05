package com.wkk.community.controller;

import com.wkk.community.entity.User;
import com.wkk.community.service.FollowService;
import com.wkk.community.service.UserService;
import com.wkk.community.util.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * @Time: 2020/5/4下午2:43
 * @Author: kongwiki
 * @Email: kongwiki@163.com
 */
@Controller
public class FollowController implements CommunityConstant {
    @Autowired
    private FollowService followService;
    @Autowired
    private HostHolder hostHolder;
    @Autowired
    private UserService userService;

    @RequestMapping(value = "/follow", method = RequestMethod.POST)
    @ResponseBody
    public String getFollow(int entityType, int entityId){
        User user = hostHolder.getUser();
        followService.follow(user.getId(), entityType, entityId);

        return CommunityUtil.getJsonString(0, "已关注");

    }

    @RequestMapping(value = "/unfollow", method = RequestMethod.POST)
    @ResponseBody
    public String unFollow(int entityType, int entityId){
        User user = hostHolder.getUser();
        followService.unFollow(user.getId(), entityType, entityId);
        return CommunityUtil.getJsonString(0, "已取消关注");

    }

    // 用户关注的人
    @RequestMapping(value = "/followee/{userId}", method = RequestMethod.GET)
    public String getUserFollowee(@PathVariable("userId") int userId, Model model, Page page){
        User user = userService.findUserById(userId);
        if(user == null){
            throw new RuntimeException("该用户不存在");
        }
        model.addAttribute("user", user);
        long followeeCount = followService.findFolloweeCount(userId, ENTITY_TYPE_USER);
        page.setLimit(5);
        page.setPath("/followee/" + userId);
        page.setRows((int)followeeCount);

        List<Map<String, Object>> followees = followService.findFollowees(userId, page.getOffset(), page.getLimit());
        if(followees != null){
            for (Map<String, Object> followee : followees) {
                User u = (User) followee.get("user");
                followee.put("hasFollowed", hasFollowed(u.getId()));
            }
        }
        model.addAttribute("users", followees);
        return "site/followee";


    }

    // 用户的粉丝
    @RequestMapping(value = "/follower/{userId}", method = RequestMethod.GET)
    public String getUserFollower(@PathVariable("userId") int userId, Model model, Page page){
        User user = userService.findUserById(userId);
        if(user == null){
            throw new RuntimeException("该用户不存在");
        }
        model.addAttribute("user", user);
        long followerCount = followService.findFollowerCount(ENTITY_TYPE_USER, userId);
        page.setPath("/follower/" + userId);
        page.setRows((int)followerCount);
        page.setLimit(5);
        List<Map<String, Object>> followers = followService.findFollowers(userId, page.getOffset(), page.getLimit());
        if(followers != null){
            for (Map<String, Object> follower : followers) {
                User u = (User) follower.get("user");
                follower.put("hasFollowed", hasFollowed(u.getId()));
            }
        }
        model.addAttribute("users", followers);
        return "site/follower";


    }



    private boolean hasFollowed(int userId){
        if(hostHolder.getUser()  == null){
            return false;
        }
        return followService.hasFollowered(hostHolder.getUser().getId(), ENTITY_TYPE_USER, userId);
    }

}
