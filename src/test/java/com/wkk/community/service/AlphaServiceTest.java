package com.wkk.community.service;

import com.wkk.community.CommunityApplication;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @Time: 2020/5/1下午2:37
 * @Author: kongwiki
 * @Email: kongwiki@163.com
 */
@SpringBootTest
@RunWith(SpringRunner.class)
@ContextConfiguration(classes = CommunityApplication.class)
class AlphaServiceTest {

    @Autowired
    private AlphaService alphaService;

    @Test
    void save() {
        Object o = alphaService.save1();
        System.out.println(o);
    }

    @Test
    public void save2(){
        Object o = alphaService.save2();
        System.out.println(o);
    }
}
