package com.wkk.community.service;

import com.wkk.community.dao.DiscussPostMapper;
import com.wkk.community.entity.DiscussPost;
import com.wkk.community.util.SensitiveFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.util.HtmlUtils;

import javax.swing.text.html.HTML;
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
    @Autowired
    private SensitiveFilter sensitiveFilter;


    public List<DiscussPost> findDiscussPosts(int userId, int offset, int limit){
       return discussPostMapper.selectDiscussPosts(userId, offset, limit);
    }

    public int findDiscussPostRows(int userId){
        return discussPostMapper.selectDiscussPostrows(userId);
    }

    // 添加post
    public int addDiscussPost(DiscussPost discussPost){
        if(discussPost == null){
            throw new IllegalArgumentException("参数不能为空");
        }
        // 转义html标记
        discussPost.setTitle(HtmlUtils.htmlEscape(discussPost.getTitle()));
        discussPost.setContent(HtmlUtils.htmlEscape(discussPost.getContent()));
        // 过滤敏感词
        discussPost.setTitle(sensitiveFilter.filter(discussPost.getTitle()));
        discussPost.setContent(sensitiveFilter.filter(discussPost.getContent()));
        // 插入数据
        return discussPostMapper.insertDiscussPost(discussPost);

    }

    // post详情
    public DiscussPost findDiscussPostById(int id){
        return discussPostMapper.selectDiscussPostById(id);
    }
    // 置顶
    public int updateType(int id, int type){
        return discussPostMapper.updateType(id, type);
    }
    // 加精
    public int updateStatus(int id, int status){
        return discussPostMapper.updateStatus(id, status);
    }
}
