package com.wkk.community.caffeine;

import com.wkk.community.entity.DiscussPost;
import com.wkk.community.service.DiscussPostService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;

/**
 * @Time: 2020/5/8下午7:47
 * @Author: kongwiki
 * @Email: kongwiki@163.com
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@ContextConfiguration
public class CaffeineTest{
    @Autowired
    private DiscussPostService discussPostService;

    public Object object = new Object();


    @Test
    public void initDataForTest() {
        for (int i = 0; i < 270000; i++) {
            DiscussPost discussPost = new DiscussPost();
            discussPost.setUserId(161);
            discussPost.setCreateTime(new Date());
            discussPost.setTitle("Caffeine测试数据初始化");
            discussPost.setContent("呜啦啦啦HK");
            discussPost.setScore(Math.random() * 2000);
            discussPostService.addDiscussPost(discussPost);
        }
    }

    @Test
    public void testCache(){
        System.out.println(discussPostService.findDiscussPosts(0, 0, 10, 1));
        System.out.println(discussPostService.findDiscussPosts(0, 0, 10, 1));
        System.out.println(discussPostService.findDiscussPosts(0, 0, 10, 1));
        System.out.println(discussPostService.findDiscussPosts(0, 0, 10, 0));
    }



}
