package com.wkk.community.util;

import com.wkk.community.CommunityApplication;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.annotation.KafkaHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @Time: 2020/5/5下午8:02
 * @Author: kongwiki
 * @Email: kongwiki@163.com
 */
@SpringBootTest
@RunWith(SpringRunner.class)
@ContextConfiguration(classes = CommunityApplication.class)
public class KafkaTest {
    @Autowired
    private Producer producer;
    @Autowired
    private Consumer consumer;

    @Test
    public void testKafka() throws InterruptedException {
        producer.sendMessage("test", "这是第一条测试数据");
        producer.sendMessage("test", "我是维坤坤");
        Thread.sleep(1000 * 10);


    }
}
@Component
class Producer {
    @Autowired
    private KafkaTemplate kafkaTemplate;

    public void sendMessage(String topic, String content){
        kafkaTemplate.send(topic, content);
    }
}

@Component
class Consumer{
    @KafkaListener(topics = {"test"})
    public void handleMessage(ConsumerRecord consumerRecord){
        System.out.println(consumerRecord.value());
    }
}
