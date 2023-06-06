package com.zz.messagepush.handler.flowcontrol.impl;

import com.alibaba.fastjson.JSONObject;
import com.google.common.util.concurrent.RateLimiter;
import com.zz.messagepush.common.constant.AustinConstant;
import com.zz.messagepush.common.domain.dto.TaskInfo;
import com.zz.messagepush.common.enums.ChannelType;
import com.zz.messagepush.handler.anno.LocalRateLimit;
import com.zz.messagepush.handler.enums.RateLimitStrategy;
import com.zz.messagepush.handler.flowcontrol.FlowControlParam;
import com.zz.messagepush.handler.flowcontrol.FlowControlService;
import com.zz.messagepush.support.service.ConfigService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Description
 * @Author 张卫刚
 * @Date Created on 2023/4/18
 */

@Service
@Slf4j
public class FlowControlFactory implements ApplicationContextAware {

    private static final String FLOW_CONTROL_KEY = "flowControlRule";

    private static final String PREFIX = "flow_control_";

    private final Map<RateLimitStrategy, FlowControlService> flowControlServiceMap = new ConcurrentHashMap<>();

    @Autowired
    private ConfigService config;

    private ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }


    @PostConstruct
    private void init() {
        Map<String, Object> serviceMap = applicationContext.getBeansWithAnnotation(LocalRateLimit.class);
        serviceMap.forEach((name, service) -> {
            if (service instanceof FlowControlService) {
                LocalRateLimit localRateLimit = service.getClass().getAnnotation(LocalRateLimit.class);
                RateLimitStrategy rateLimitStrategy = localRateLimit.rateLimitStrategy();
                //通常情况下 实现限流的service与rateLimitStrategy一一对应
                flowControlServiceMap.put(rateLimitStrategy, (FlowControlService) service);
            }
        });
    }

    public void flowControl(TaskInfo taskInfo, FlowControlParam flowControlParam) {
        RateLimiter rateLimiter;
        Double rateInitValue = flowControlParam.getRateInitValue();
        Double limitStrategy = getRateLimitStrategy(taskInfo.getSendChannel());
        if (limitStrategy != null && !rateInitValue.equals(limitStrategy)) {
            rateLimiter = RateLimiter.create(limitStrategy);
            flowControlParam.setRateInitValue(limitStrategy);
            flowControlParam.setRateLimiter(rateLimiter);
        }
        FlowControlService flowControlService = flowControlServiceMap.get(flowControlParam.getRateLimitStrategy());
        if (Objects.isNull(flowControlService)) {
            log.error("没有找到对应的单机限流策略");
            return ;
        }
        double costTime = flowControlService.flowControl(taskInfo, flowControlParam);
        if (costTime > 0) {
            log.info("consumer {} flow control time {}", Objects.requireNonNull(ChannelType.getEnumByCode(taskInfo.getSendChannel())).getDescription(),costTime);
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
        if (jsonObject.getDouble(PREFIX + channelCode) == null) {
            return null;
        }
        return jsonObject.getDouble(PREFIX + channelCode);
    }
}
