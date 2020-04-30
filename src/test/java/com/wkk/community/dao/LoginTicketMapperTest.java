package com.wkk.community.dao;

import com.wkk.community.CommunityApplication;
import com.wkk.community.entity.LoginTicket;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;

/**
 * @Time: 2020/4/30上午11:41
 * @Author: kongwiki
 * @Email: kongwiki@163.com
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@ContextConfiguration(classes = CommunityApplication.class)
class LoginTicketMapperTest {
    @Autowired
    private LoginTicketMapper loginTicketMapper;

    @Test
    public void testInsert() {
        LoginTicket loginTicket = new LoginTicket();
        loginTicket.setExpired(new Date(System.currentTimeMillis() + 1000 * 60 * 10));
        loginTicket.setStatus(0);
        loginTicket.setTicket("abc");
        loginTicket.setUserId(101);
        loginTicketMapper.insertLoginTicket(loginTicket);
    }

    @Test
    public void testSelect(){
        LoginTicket loginTicket = loginTicketMapper.selectByTicket("abc");
        System.out.println(loginTicket.getTicket());
    }

    @Test
    public void updateTest(){
        loginTicketMapper.updateStatus("abc", 1);
    }
}
