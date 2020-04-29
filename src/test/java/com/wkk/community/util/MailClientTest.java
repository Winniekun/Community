package com.wkk.community.util;

import com.wkk.community.CommunityApplication;
import org.apache.coyote.http11.upgrade.UpgradeServletOutputStream;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @Time: 2020/4/29下午7:41
 * @Author: kongwiki
 * @Email: kongwiki@163.com
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@ContextConfiguration(classes = CommunityApplication.class)
class MailClientTest {
    @Autowired
    private MailClient mailClient;
    @Autowired
    private TemplateEngine templateEngine;


    @Test
    public void testMail(){
        mailClient.sendMail("kongwiki5@gmail.com", "SpringBoot邮箱整合", "成功");
    }

    @Test
    public void testMailWithHtml(){
        Context context = new Context();
        context.setVariable("username", "哈喽哈喽嘿");

        String content = templateEngine.process("/mail/demo", context);
        System.out.println(content);
        mailClient.sendMail("kongwiki@163.com", "Spring邮箱整合---HTML", content);

    }
}
