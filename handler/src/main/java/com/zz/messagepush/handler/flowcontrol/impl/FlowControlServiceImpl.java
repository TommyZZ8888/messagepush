package com.zz.messagepush.handler.flowcontrol.impl;

import com.alibaba.fastjson.JSONObject;
import com.ctrip.framework.apollo.Config;
import com.ctrip.framework.apollo.spring.annotation.ApolloConfig;
import com.google.common.util.concurrent.RateLimiter;
import com.zz.messagepush.common.constant.AustinConstant;
import com.zz.messagepush.common.domain.dto.TaskInfo;
import com.zz.messagepush.common.utils.EnumUtil;
import com.zz.messagepush.handler.enums.RateLimitStrategy;
import com.zz.messagepush.handler.flowcontrol.FlowControlParam;
import com.zz.messagepush.handler.flowcontrol.FlowControlService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @Description
 * @Author 张卫刚
 * @Date Created on 2023/4/18
 */

@Service
@Slf4j
public class FlowControlServiceImpl implements FlowControlService {


    private static final String FLOW_CONTROL_KEY = "flowControl";

    private static final String PREFIX = "flow_control_";

    @ApolloConfig("boss.austin")
    private Config config;


    @Override
    public void flowControl(TaskInfo taskInfo, FlowControlParam flowControlParam) {
        RateLimiter rateLimiter = flowControlParam.getRateLimiter();
        Double rateInitValue = flowControlParam.getRateInitValue();
        RateLimitStrategy rateLimitStrategy = flowControlParam.getRateLimitStrategy();

        double costTime = 0;

        Double limitStrategy = getRateLimitStrategy(taskInfo.getSendChannel());
        if (limitStrategy != null && !rateInitValue.equals(limitStrategy)) {
            rateLimiter = RateLimiter.create(limitStrategy);
            flowControlParam.setRateInitValue(limitStrategy);
            flowControlParam.setRateLimiter(rateLimiter);
        }
        if (rateLimitStrategy.equals(RateLimitStrategy.REQUEST_RATE_LIMIT)) {
            costTime = rateLimiter.acquire(1);
        }
        if (rateLimitStrategy.equals(RateLimitStrategy.SEND_USER_NUM_RATE_LIMIT)) {
            costTime = rateLimiter.acquire(taskInfo.getReceiver().size());
        }
        if (costTime > 0) {
            log.info("consumer {} flow control time {}", EnumUtil.getValue(RateLimitStrategy.class, taskInfo.getSendChannel()), costTime);
        }
    }


    /**
     * 得到限流值的配置
     * apollo配置样例     key：flowControl value：{"flow_control_40":1}
     * 渠道枚举可看：com.java3y.austin.common.enums.ChannelType
     *
     * @param channelCode
     */
    private Double getRateLimitStrategy(Integer channelCode) {
        String property = config.getProperty(FLOW_CONTROL_KEY, AustinConstant.APOLLO_DEFAULT_VALUE_JSON_OBJECT);
        JSONObject jsonObject = JSONObject.parseObject(property);
        if (jsonObject.getDouble(FLOW_CONTROL_KEY + channelCode) == null) {
            return null;
        }
        return jsonObject.getDouble(FLOW_CONTROL_KEY + channelCode);
    }
}
