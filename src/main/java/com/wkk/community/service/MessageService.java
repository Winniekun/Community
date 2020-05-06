package com.wkk.community.service;

import com.wkk.community.dao.MessageMapper;
import com.wkk.community.entity.Message;
import com.wkk.community.util.SensitiveFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.util.HtmlUtils;

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

    @Autowired
    private SensitiveFilter sensitiveFilter;


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

    // 添加消息
    public int addMessage(Message message){
        message.setContent(HtmlUtils.htmlEscape(message.getContent()));
        message.setContent(sensitiveFilter.filter(message.getContent()));

        return messageMapper.insertMessage(message);
    }

    // 消息变为已读
    public int readMessage(List<Integer> ids){
        return messageMapper.updateStatus(ids, 1);
    }

    // 查询某个主题下最新的通知
    public Message findLatestNotice(int userId, String topic){
        return messageMapper.selectLatestNotice(userId, topic);
    }

    // 查询某个主题所包含的通知数量
    public int findNoticeCount(int userId, String topic){
        return messageMapper.selectNoticeCount(userId, topic);
    }

    // 未读通知数量
    public int findNoticeUnReadCount(int userId, String topic){
        return messageMapper.selectNoticeUnReadCount(userId, topic);
    }

    public List<Message> findNotices(int useId, String topic, int offset, int limit){
        return messageMapper.selectNotices(useId, topic, offset, limit);
    }

}
