package com.zz.messagepush.handler.flowcontrol;

import com.google.common.util.concurrent.RateLimiter;
import com.zz.messagepush.handler.enums.RateLimitStrategy;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Description 流量控制所需要的参数
 * @Author 张卫刚
 * @Date Created on 2023/4/18
 */

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FlowControlParam {

    /**
     * 限流器
     * 子类初始化时指定
     */
    protected RateLimiter rateLimiter;

    /**
     * 限流器初始值大小
     */
    protected Double rateInitValue;

    /**
     * 限流的策略
     */
    protected RateLimitStrategy rateLimitStrategy;

}
