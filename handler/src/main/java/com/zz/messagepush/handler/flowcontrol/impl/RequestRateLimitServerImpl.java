package com.zz.messagepush.handler.flowcontrol.impl;

import com.google.common.util.concurrent.RateLimiter;
import com.zz.messagepush.common.domain.dto.TaskInfo;
import com.zz.messagepush.handler.anno.LocalRateLimit;
import com.zz.messagepush.handler.enums.RateLimitStrategy;
import com.zz.messagepush.handler.flowcontrol.FlowControlParam;
import com.zz.messagepush.handler.flowcontrol.FlowControlService;

/**
 * @Description RequestRateLimitServer
 * @Author 张卫刚
 * @Date Created on 2023/6/6
 */
@LocalRateLimit(rateLimitStrategy = RateLimitStrategy.REQUEST_RATE_LIMIT)
public class RequestRateLimitServerImpl implements FlowControlService {
    @Override
    public double flowControl(TaskInfo taskInfo, FlowControlParam flowControlParam) {
        RateLimiter rateLimiter = flowControlParam.getRateLimiter();
        return rateLimiter.acquire(1);
    }
}
