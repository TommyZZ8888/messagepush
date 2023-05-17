package com.zz.messagepush.support.mq.eventBus;

import com.zz.messagepush.common.domain.dto.TaskInfo;
import com.zz.messagepush.support.domain.entity.MessageTemplateEntity;

import java.util.List;

/**
 * @Description 监听器
 * @Author 张卫刚
 * @Date Created on 2023/5/17
 */
public interface EventBusListener {

    /**
     * 消费消息
     * @param list
     */
    void consumer(List<TaskInfo> list);


    /**
     * 撤回消息
     * @param messageTemplateEntity
     */
    void recall(MessageTemplateEntity messageTemplateEntity);

}
