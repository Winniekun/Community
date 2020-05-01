package com.wkk.community.service;

import com.wkk.community.dao.CommentMapper;
import com.wkk.community.dao.DiscussPostMapper;
import com.wkk.community.entity.Comment;
import com.wkk.community.util.CommunityConstant;
import com.wkk.community.util.SensitiveFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.util.HtmlUtils;

import java.util.List;

/**
 * @Time: 2020/5/1下午5:25
 * @Author: kongwiki
 * @Email: kongwiki@163.com
 */
@Service
public class CommentService implements CommunityConstant {
    @Autowired(required = false)
    private CommentMapper commentMapper;
    @Autowired(required = false)
    private DiscussPostMapper discussPostMapper;
    @Autowired
    private SensitiveFilter sensitiveFilter;

    public List<Comment> findCommentByEntity(int entityType, int entityId, int offset, int limit) {
        return commentMapper.selectCommentsByEntity(entityType, entityId, offset, limit);
    }

    public int findCountByEntity(int entityType, int entityId) {
        return commentMapper.selectCountByEntity(entityType, entityId);
    }

    public int addComment(Comment comment) {
        if (comment == null) {
            throw new IllegalArgumentException("请输入评论");
        }
        // 转义HTML标记
        comment.setContent(HtmlUtils.htmlEscape(comment.getContent()));
        // 过滤敏感词汇
        comment.setContent(sensitiveFilter.filter(comment.getContent()));
        int rows = commentMapper.insertComment(comment);
        // 更新帖子的评论数量
        if (comment.getEntityType() == ENTITY_TYPE_POST) {
            int commentCount = commentMapper.selectCountByEntity(comment.getEntityType(), comment.getEntityId());
            discussPostMapper.updateCommentCount(comment.getEntityId(), commentCount);
        }
        return rows;
    }


}


