package com.zz.messagepush.handler.handler;

import com.zz.messagepush.common.domain.dto.TaskInfo;
import com.zz.messagepush.support.domain.entity.MessageTemplateEntity;

/**
 * @Description
 * @Author 张卫刚
 * @Date Created on 2023/3/13
 */
public interface Handler {


    /**
     * 处理器
     * @param taskInfo
     */
    void doHandler(TaskInfo taskInfo);

    /**
     * 撤回消息
     * @param messageTemplate
     */
    void recall(MessageTemplateEntity messageTemplate);

}
