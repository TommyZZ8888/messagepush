package com.zz.messagepush.handler.receiver.service;

import com.zz.messagepush.common.domain.dto.TaskInfo;
import com.zz.messagepush.support.domain.entity.MessageTemplateEntity;

import java.util.List;

/**
 * @Description ConsumerService
 * @Author 张卫刚
 * @Date Created on 2023/5/17
 */
public interface ConsumeService {

    /**
     * 从mq拉到消息进行消费，发送消息
     * @param list
     */
    void consume2Send(List<TaskInfo> list);

    /**
     * 从mq拉到消息进行消费，撤回消息
     * @param messageTemplateEntity
     */
    void consume2Recall(MessageTemplateEntity messageTemplateEntity);
}
