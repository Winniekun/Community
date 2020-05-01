package com.wkk.community.service;

import com.wkk.community.dao.MessageMapper;
import com.wkk.community.entity.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Time: 2020/5/1下午9:44
 * @Author: kongwiki
 * @Email: kongwiki@163.com
 */
@Service
public class MessageService {
    @Autowired(required = false)
    private MessageMapper messageMapper;

    public List<Message> findConversations(int userId, int offset, int limit){
        return messageMapper.selectConversations(userId, offset, limit);
    }

    public int findConversationCount(int userId){
        return messageMapper.selectConversationCount(userId);
    }

    public List<Message> findLetters(String conversationId, int offset, int limit){
        return messageMapper.selectLetters(conversationId,offset, limit);
    }

    public int findLetterCount(String conversationId){
        return messageMapper.selectLetterCount(conversationId);
    }

    public int findLetterUnReadCount(int userId,  String conversationId){
        return messageMapper.selectLetterUnReadCount(userId, conversationId);
    }

}
