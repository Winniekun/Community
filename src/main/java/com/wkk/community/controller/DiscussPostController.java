package com.wkk.community.controller;

import com.wkk.community.annotation.LoginRequired;
import com.wkk.community.entity.DiscussPost;
import com.wkk.community.entity.User;
import com.wkk.community.service.DiscussPostService;
import com.wkk.community.util.CommunityUtil;
import com.wkk.community.util.HostHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Date;

/**
 * @Time: 2020/5/1上午11:27
 * @Author: kongwiki
 * @Email: kongwiki@163.com
 */
@Controller
@RequestMapping("/discuss")
public class DiscussPostController {
    @Autowired
    private DiscussPostService discussPostService;
    @Autowired
    private HostHolder hostHolder;

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

        // 报错的情况， 将来程序统一处理
        return CommunityUtil.getJsonString(0, "发布成功");


    }

}
