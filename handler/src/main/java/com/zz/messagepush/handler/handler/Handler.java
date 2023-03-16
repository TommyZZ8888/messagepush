package com.zz.messagepush.handler.handler;

import com.zz.messagepush.common.domain.TaskInfo;

/**
 * @Description
 * @Author 张卫刚
 * @Date Created on 2023/3/13
 */
public interface Handler {
    /**
     * 消息处理
     * @param taskInfoDTO
     * @return
     */
    boolean doHandler(TaskInfo taskInfoDTO);
}
