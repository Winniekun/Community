package com.wkk.community.service;

import com.wkk.community.CommunityApplication;
import com.wkk.community.pojo.DiscussPost;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @Time: 2020/4/29上午10:53
 * @Author: kongwiki
 * @Email: kongwiki@163.com
 */
@RunWith(SpringRunner.class)
@ContextConfiguration(classes = CommunityApplication.class)
@SpringBootTest
class DiscussPostServiceTest {
    @Autowired
    private DiscussPostService service;

    @Test
    public void testFind(){
//        int discussPostRows = service.findDiscussPostRows(101);
//        System.out.println(discussPostRows);
        List<DiscussPost> discussPosts = service.findDiscussPosts(0, 0, 0);
        for (DiscussPost discussPost : discussPosts) {
            System.out.println("+++++++++++++++++++++++++++++++++++++++++++");
            System.out.println(discussPost);
        }
    }
}
