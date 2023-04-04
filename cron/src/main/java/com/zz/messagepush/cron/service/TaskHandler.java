package com.zz.messagepush.cron.service;

/**
 * @Description 具体处理定时任务逻辑的handler
 * @Author 张卫刚
 * @Date Created on 2023/4/4
 */
public interface TaskHandler {

    /**
     * 处理具体定时任务逻辑
     * @param messageTemplateId
     */
    void handler(Long messageTemplateId);
}
