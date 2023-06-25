package com.zz.messagepush.support.mq.springeventbus;

import com.zz.messagepush.support.constant.MessageQueuePipeline;
import com.zz.messagepush.support.mq.SendMqService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

/**
 * @Description SpringEventBusSendMqServiceImpl
 * @Author 张卫刚
 * @Date Created on 2023/6/25
 */

@Slf4j
@Component
@ConditionalOnProperty(value = "austin.mq.pipeline", havingValue = MessageQueuePipeline.SPRING_EVENT_BUS)
public class SpringEventBusSendMqServiceImpl implements SendMqService {

    @Autowired
    private ApplicationContext applicationContext;

    @Override
    public void send(String topic, String jsonValue, String tagId) {
        SpringEventBusEvent springEventBusEvent = new SpringEventBusEvent(1);
        springEventBusEvent.setJsonValue(jsonValue);
        springEventBusEvent.setTagId(tagId);
        springEventBusEvent.setTopic(topic);
        applicationContext.publishEvent(applicationContext);
    }

    @Override
    public void send(String topic, String jsonValue) {
        send(topic, jsonValue, null);
    }
}
