package com.zz.messagepush.handler.receiver.rabbitmq;

import com.alibaba.fastjson.JSON;
import com.zz.messagepush.common.domain.dto.TaskInfo;
import com.zz.messagepush.handler.receiver.service.ConsumeService;
import com.zz.messagepush.support.constant.MessageQueuePipeline;
import com.zz.messagepush.support.domain.entity.MessageTemplateEntity;
import org.apache.commons.lang3.StringUtils;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @Description RabbitMqReceiver
 * @Author 张卫刚
 * @Date Created on 2023/5/19
 */

@Component
@ConditionalOnProperty(name = "austin-mq-pipeline", havingValue = MessageQueuePipeline.RABBIT_MQ)
public class RabbitMqReceiver {

    @Autowired
    private ConsumeService consumeService;


    @RabbitListener(queues = "austinRabbit")
    public void receive(String message) {
        if (StringUtils.isBlank(message)) {
            return;
        }
        List<TaskInfo> taskInfos = JSON.parseArray(message, TaskInfo.class);
        consumeService.consume2Send(taskInfos);
    }

    @RabbitListener(queues = "austinRabbit")
    public void recall(String message) {
        if (StringUtils.isBlank(message)) {
            return;
        }
        MessageTemplateEntity messageTemplateEntity = JSON.parseObject(message, MessageTemplateEntity.class);
        consumeService.consume2Recall(messageTemplateEntity);
    }
}
