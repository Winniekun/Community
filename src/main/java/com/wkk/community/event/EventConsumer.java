package com.wkk.community.event;

import com.alibaba.fastjson.JSONObject;
import com.wkk.community.entity.Event;
import com.wkk.community.entity.Message;
import com.wkk.community.service.MessageService;
import com.wkk.community.service.UserService;
import com.wkk.community.util.CommunityConstant;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @Time: 2020/5/5下午9:55
 * @Author: kongwiki
 * @Email: kongwiki@163.com
 */
@Component
public class EventConsumer implements CommunityConstant {
    private static final Logger logger = LoggerFactory.getLogger(EventConsumer.class);
    @Autowired
    private MessageService messageService;


    @KafkaListener(topics = {TOPIC_COMMENT, TOPIC_FOLLOW, TOPIC_LIKE})
    public void handleCommentMSG(ConsumerRecord consumerRecord){
        if(consumerRecord == null || consumerRecord.value() == null){
            logger.error("消息的内容为空");
            return;
        }
        Event event = JSONObject.parseObject(consumerRecord.value().toString(), Event.class);
        if(event == null){
            logger.error("消息格式错误");
            return;
        }

        // 发送站内通知
        Message message = new Message();
        message.setFromId(SYSTEM_USER_ID);
        message.setToId(event.getEntityUserId());
        message.setConversationId(event.getTopic());
        message.setCreateTime(new Date());
        Map<String, Object> content = new HashMap<>();
        content.put("userId", event.getUserId());
        content.put("entityType", event.getEntityType());
        content.put("entityId", event.getEntityId());

        if(!event.getData().isEmpty()){
            for (Map.Entry<String, Object> entry : event.getData().entrySet()) {
                content.put(entry.getKey(), entry.getValue());
            }
        }
        message.setContent(JSONObject.toJSONString(content));
        messageService.addMessage(message);


    }

}
