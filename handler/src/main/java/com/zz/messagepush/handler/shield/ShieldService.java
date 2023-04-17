package com.zz.messagepush.handler.shield;

import com.zz.messagepush.common.domain.dto.TaskInfo;

/**
 * @Description 屏蔽服务
 * @Author 张卫刚
 * @Date Created on 2023/4/14
 */
public interface ShieldService {

    /**
     * 屏蔽
     * @param taskInfo
     */
    void shield(TaskInfo taskInfo);
}
