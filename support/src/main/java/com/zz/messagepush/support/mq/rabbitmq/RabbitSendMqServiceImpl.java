package com.zz.messagepush.support.mq.rabbitmq;

import com.zz.messagepush.common.domain.dto.TaskInfo;
import com.zz.messagepush.support.constant.MessageQueuePipeline;
import com.zz.messagepush.support.domain.entity.MessageTemplateEntity;
import com.zz.messagepush.support.mq.SendMqService;
import com.zz.messagepush.support.mq.eventBus.EventBusListener;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

/**
 * @Description RabbitMqServiceImpl
 * @Author 张卫刚
 * @Date Created on 2023/5/19
 */
@Slf4j
@Service
@ConditionalOnProperty(name = "austin-mq-pipeline", havingValue = MessageQueuePipeline.RABBIT_MQ)
public class RabbitSendMqServiceImpl implements SendMqService {


    @Value("${austin.rabbitmq.topic.name}")
    private String topicName;

    @Value("${austin.rabbitmq.exchange.name}")
    private String exchangeName;

    @Autowired
    private RabbitTemplate rabbitTemplate;


    @Override
    public void send(String topic, String jsonValue, String tagId) {
        if (topic.equals(topicName)) {
            rabbitTemplate.convertAndSend(exchangeName, topicName, jsonValue);
        } else {
            log.error("RabbitSendMqServiceImpl send topic error! topic:{},confTopic:{}", topic, topicName);
        }
    }

    @Override
    public void send(String topic, String jsonValue) {
        send(topic, jsonValue);
    }
}
