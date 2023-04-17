package com.zz.messagepush.cron.handler;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.xxl.job.core.handler.annotation.XxlJob;
import com.zz.messagepush.common.domain.dto.TaskInfo;
import com.zz.messagepush.support.config.SupportThreadPoolConfig;
import com.zz.messagepush.support.utils.RedisUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;

import java.util.Arrays;
import java.util.List;

/**
 * @Description
 * @Author 张卫刚
 * @Date Created on 2023/4/14
 */
public class NightShieldLazyPendingHandler {

    private static final String NIGHT_SHIELD_BUT_NEXT_DAY_SEND_KEY = "night_shield_send";


    @Autowired
    private KafkaTemplate kafkaTemplate;

    @Autowired
    private RedisUtil redisUtil;

    @Value("${austin.business.topic.name}")
    private String topicName;


    /**
     * 处理 夜间 屏蔽，次日早上九点发送信息
     */
    @XxlJob("nightShieldLazyJob")
    public void execute() {
        SupportThreadPoolConfig.getPendingSingleThreadPool().execute(() -> {
            while (redisUtil.lLen(NIGHT_SHIELD_BUT_NEXT_DAY_SEND_KEY) > 0) {
                String taskInfo = redisUtil.lPop(NIGHT_SHIELD_BUT_NEXT_DAY_SEND_KEY);

                if (StringUtils.isNotBlank(taskInfo)) {
                    kafkaTemplate.send(topicName, JSONObject.toJSONString(List.of(JSON.parseObject(taskInfo, TaskInfo.class)), SerializerFeature.WriteClassName));
                }
            }
        });
    }
}

