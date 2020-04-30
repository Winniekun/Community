package com.wkk.community.service;

import com.wkk.community.dao.DiscussPostMapper;
import com.wkk.community.entity.DiscussPost;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Time: 2020/4/29上午10:42
 * @Author: kongwiki
 * @Email: kongwiki@163.com
 */
@Service
public class DiscussPostService {
    @Autowired(required = false)
    private DiscussPostMapper discussPostMapper;

    public List<DiscussPost> findDiscussPosts(int userId, int offset, int limit){
       return discussPostMapper.selectDiscussPosts(userId, offset, limit);
    }

    public int findDiscussPostRows(int userId){
        return discussPostMapper.selectDiscussPostrows(userId);
    }
}
