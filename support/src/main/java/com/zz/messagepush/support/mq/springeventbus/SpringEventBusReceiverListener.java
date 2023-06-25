package com.zz.messagepush.support.mq.springeventbus;

import com.alibaba.fastjson.JSON;
import com.zz.messagepush.common.domain.dto.TaskInfo;
import com.zz.messagepush.handler.receiver.springeventbus.SpringEventBusReceiver;
import com.zz.messagepush.support.constant.MessageQueuePipeline;
import com.zz.messagepush.support.domain.entity.MessageTemplateEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Service;

/**
 * @Description SpringEventBusReceiverListener
 * @Author 张卫刚
 * @Date Created on 2023/6/25
 */
@Service
@ConditionalOnProperty(name = "austin.mq.pipeline", havingValue = MessageQueuePipeline.SPRING_EVENT_BUS)
public class SpringEventBusReceiverListener implements ApplicationListener<SpringEventBusEvent> {
    @Autowired
    private SpringEventBusReceiver springEventBusReceiver;

    @Value("${austin.business.topic.name}")
    private String sendTopic;
    @Value("${austin.business.recall.topic.name}")
    private String recallTopic;

    @Override
    public void onApplicationEvent(SpringEventBusEvent event) {
        String  topic  = event.getTopic();
        String jsonValue = event.getJsonValue();
        if (topic.equals(sendTopic)) {
            springEventBusReceiver.consumer(JSON.parseArray(jsonValue, TaskInfo.class));
        } else if (topic.equals(recallTopic)) {
            springEventBusReceiver.recall(JSON.parseObject(jsonValue, MessageTemplateEntity.class));
        }
    }
}
