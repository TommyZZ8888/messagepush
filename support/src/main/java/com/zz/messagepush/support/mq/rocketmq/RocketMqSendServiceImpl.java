package com.zz.messagepush.support.mq.rocketmq;

import com.alibaba.fastjson.JSON;
import com.zz.messagepush.support.constant.MessageQueuePipeline;
import com.zz.messagepush.support.mq.SendMqService;
import org.apache.commons.lang3.StringUtils;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

/**
 * @Description RocketMqSendServiceImpl
 * @Author 张卫刚
 * @Date Created on 2023/5/19
 */

@Service
@ConditionalOnProperty(name = "austin-mq-pipeline", havingValue = MessageQueuePipeline.ROCKET_MQ)
public class RocketMqSendServiceImpl implements SendMqService {

    @Autowired
    private RocketMQTemplate rocketMQTemplate;

    @Override
    public void send(String topic, String jsonValue, String tagId) {
        if (StringUtils.isNotBlank(tagId)) {
            topic = topic + ":" + tagId;
        }
        send(topic, jsonValue);
    }

    @Override
    public void send(String topic, String jsonValue) {
        rocketMQTemplate.send(topic, MessageBuilder.withPayload(jsonValue).build());
    }
}
