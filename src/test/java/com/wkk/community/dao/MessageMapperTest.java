package com.wkk.community.dao;

import com.wkk.community.CommunityApplication;
import com.wkk.community.entity.Message;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @Time: 2020/5/1下午10:19
 * @Author: kongwiki
 * @Email: kongwiki@163.com
 */
@SpringBootTest
@RunWith(SpringRunner.class)
@ContextConfiguration(classes = CommunityApplication.class)
class MessageMapperTest {
    @Autowired
    private MessageMapper messageMapper;
    @Test
    public void testSelectMessage(){
//        List<Message> messages = messageMapper.selectConversations(111, 0, 10);
//        for (Message message : messages) {
//            System.out.println(message.getContent());
//        }
//        int i = messageMapper.selectConversationCount(111);
//        System.out.println(i);
//
//        int i1 = messageMapper.selectLetterCount("111_112");
//        System.out.println(i1);

//        List<Message> letters = messageMapper.selectLetters("111_112", 0, 10);
//        for (Message letter : letters) {
//            System.out.println(letter.getContent());
//        }
//
        int i2 = messageMapper.selectLetterUnReadCount(111, "111_112");
        System.out.println(i2);
    }
}
