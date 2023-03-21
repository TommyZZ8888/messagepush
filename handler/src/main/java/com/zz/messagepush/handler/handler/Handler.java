package com.zz.messagepush.handler.handler;

import com.zz.messagepush.common.domain.dto.TaskInfo;

/**
 * @Description
 * @Author 张卫刚
 * @Date Created on 2023/3/13
 */
public interface Handler {
    /**
     * 消息处理
     * @param taskInfo
     * @return
     */
    boolean doHandler(TaskInfo taskInfo);
}
