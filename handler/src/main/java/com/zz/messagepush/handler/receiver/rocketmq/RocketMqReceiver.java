package com.zz.messagepush.handler.receiver.rocketmq;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.zz.messagepush.common.domain.dto.TaskInfo;
import com.zz.messagepush.handler.receiver.service.ConsumeService;
import com.zz.messagepush.support.constant.MessageQueuePipeline;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.annotation.SelectorType;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

/**
 * @Description RocketMqReceiver
 * @Author 张卫刚
 * @Date Created on 2023/6/6
 */

@Component
@ConditionalOnProperty(name = "austin-ma-pipeline", havingValue = MessageQueuePipeline.ROCKET_MQ)
@RocketMQMessageListener(topic = "${austin.topic.business.topic}",
        consumerGroup = "${austin.rocketmq.consumer.group}",
        selectorType = SelectorType.TAG, selectorExpression = "${austin.business.tagId.value}")
public class RocketMqReceiver implements RocketMQListener<String> {

    @Autowired
    private ConsumeService consumeService;

    @Override
    public void onMessage(String s) {
        if (StrUtil.isBlank(s)) {
            return;
        }
        consumeService.consume2Send(JSON.parseArray(s, TaskInfo.class));
    }
}
