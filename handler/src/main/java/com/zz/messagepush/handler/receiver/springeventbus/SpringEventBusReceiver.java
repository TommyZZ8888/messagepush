package com.zz.messagepush.handler.receiver.springeventbus;

import com.zz.messagepush.common.domain.dto.TaskInfo;
import com.zz.messagepush.handler.receiver.service.ConsumeService;
import com.zz.messagepush.support.constant.MessageQueuePipeline;
import com.zz.messagepush.support.domain.entity.MessageTemplateEntity;
import com.zz.messagepush.support.mq.eventBus.EventBusListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @Description SpringEventBusReceiver
 * @Author 张卫刚
 * @Date Created on 2023/6/25
 */

@Component
@ConditionalOnProperty(value = "austin.mq.pipeline", havingValue = MessageQueuePipeline.SPRING_EVENT_BUS)
public class SpringEventBusReceiver implements EventBusListener {

    @Autowired
    private ConsumeService consumeService;

    @Override
    public void consumer(List<TaskInfo> list) {
        consumeService.consume2Send(list);
    }

    @Override
    public void recall(MessageTemplateEntity messageTemplateEntity) {
        consumeService.consume2Recall(messageTemplateEntity);
    }
}
