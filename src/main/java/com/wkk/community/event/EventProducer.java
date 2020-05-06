package com.wkk.community.event;

import com.alibaba.fastjson.JSONObject;
import com.wkk.community.entity.Event;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

/**
 * @Time: 2020/5/5下午9:55
 * @Author: kongwiki
 * @Email: kongwiki@163.com
 */
@Component
public class EventProducer {
    @Autowired
    private KafkaTemplate kafkaTemplate;

    //处理事件的方法
    public void fireEvent(Event event){
        // 事件发布到指定的主题
        kafkaTemplate.send(event.getTopic(), JSONObject.toJSONString(event));
    }
}
