package com.wkk.community.dao;

import com.wkk.community.CommunityApplication;
import com.wkk.community.entity.Comment;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ConcurrentModificationException;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @Time: 2020/5/1下午5:10
 * @Author: kongwiki
 * @Email: kongwiki@163.com
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@ContextConfiguration(classes = CommunityApplication.class)
class CommentMapperTest {
    @Autowired(required = false)
    private CommentMapper commentMapper;

    @Test
    public void testSelect(){
        List<Comment> comments = commentMapper.selectCommentsByEntity(1, 228, 0, 10);
        int i = 0;
        for (Comment comment : comments) {
            i++;
            System.out.println(comment.getContent() + " " + i);
        }
    }
    @Test
    public void testCount(){
        int i = commentMapper.selectCountByEntity(1, 228);
        System.out.println("总量： " + i);
    }

    @Test
    public void insertTest(){
        Comment comment = new Comment();
        comment.setUserId(154);
        comment.setStatus(0);
        comment.setContent("韩欢欢用户的测试数据");
        comment.setCreateTime(new Date());
        comment.setEntityId(228);
        comment.setEntityType(1);
        comment.setTargetId(0);
        int i = commentMapper.insertComment(comment);
        System.out.println(comment.getId());
    }

}
