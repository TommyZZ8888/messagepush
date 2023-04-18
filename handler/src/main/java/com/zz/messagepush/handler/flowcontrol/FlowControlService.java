package com.zz.messagepush.handler.flowcontrol;

import com.zz.messagepush.common.domain.dto.TaskInfo;

/**
 * @Description 流量控制服务
 * @Author 张卫刚
 * @Date Created on 2023/4/18
 */
public interface FlowControlService {

    /**
     * 根据渠道进行流量控制
     * @param taskInfo
     * @param flowControlParam
     */
    void flowControl(TaskInfo taskInfo,FlowControlParam flowControlParam);
}
