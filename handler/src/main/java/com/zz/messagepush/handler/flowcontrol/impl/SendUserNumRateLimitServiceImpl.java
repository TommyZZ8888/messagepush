package com.zz.messagepush.handler.flowcontrol.impl;

import com.google.common.util.concurrent.RateLimiter;
import com.zz.messagepush.common.domain.dto.TaskInfo;
import com.zz.messagepush.handler.anno.LocalRateLimit;
import com.zz.messagepush.handler.enums.RateLimitStrategy;
import com.zz.messagepush.handler.flowcontrol.FlowControlParam;
import com.zz.messagepush.handler.flowcontrol.FlowControlService;

/**
 * @Description SendUserNumRateLimitServiceImpl
 * @Author 张卫刚
 * @Date Created on 2023/6/6
 */

@LocalRateLimit(rateLimitStrategy = RateLimitStrategy.SEND_USER_NUM_RATE_LIMIT)
public class SendUserNumRateLimitServiceImpl implements FlowControlService {

    /**
     * 根据渠道进行流量控制
     * @param taskInfo
     * @param flowControlParam
     * @return
     */
    @Override
    public double flowControl(TaskInfo taskInfo, FlowControlParam flowControlParam) {
        RateLimiter rateLimiter = flowControlParam.getRateLimiter();
        return rateLimiter.acquire(taskInfo.getReceiver().size());
    }
}
