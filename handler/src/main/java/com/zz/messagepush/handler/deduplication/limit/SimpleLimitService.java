package com.zz.messagepush.handler.deduplication.limit;

import cn.hutool.core.collection.CollUtil;
import com.zz.messagepush.common.constant.AustinConstant;
import com.zz.messagepush.common.domain.dto.TaskInfo;
import com.zz.messagepush.handler.deduplication.AbstractDeduplicationService;
import com.zz.messagepush.handler.domain.DeduplicationParam;
import com.zz.messagepush.support.utils.RedisUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @Description 采用普通的技术去重方法，限制每天发送的条数
 * @Author 张卫刚
 * @Date Created on 2023/4/19
 */
@Service("SimpleLimitService")
public class SimpleLimitService extends AbstractLimitService {


    private final static String LIMIT_TAG = "SP_";

    @Autowired
    private RedisUtil redisUtil;


    @Override
    public Set<String> limitFilter(AbstractDeduplicationService deduplicationService, TaskInfo taskInfo, DeduplicationParam param) {
        Set<String> filterReceiver = new HashSet<>(taskInfo.getReceiver().size());
        Map<String, String> readyPutInRedisReceiver = new HashMap<>(taskInfo.getReceiver().size());

        List<String> keys = deduplicationAllKey(deduplicationService, taskInfo).stream().map(key -> LIMIT_TAG + key).collect(Collectors.toList());
        Map<String, String> inRedisValue = redisUtil.mGet(keys);

        for (String receiver : taskInfo.getReceiver()) {
            String key = LIMIT_TAG + deduplicationSingleKey(deduplicationService, taskInfo, receiver);
            String value = inRedisValue.get(key);

            //符合条件的用户
            if (value != null && Integer.parseInt(value) <= param.getCountNum()) {
                filterReceiver.add(key);
            } else {
                readyPutInRedisReceiver.put(key, receiver);
            }
        }

        putInRedis(readyPutInRedisReceiver, inRedisValue, param.getDeduplicationTime());
        return filterReceiver;
    }


    /**
     * 存入redis去重
     * @param readyPutRedisReceiver
     * @param inRedisValue
     * @param deduplicationTime
     */
    private void putInRedis(Map<String, String> readyPutRedisReceiver,
                            Map<String, String> inRedisValue, Long deduplicationTime) {
        Map<String, String> keyValues = new HashMap<>(readyPutRedisReceiver.size());
        for (Map.Entry<String, String> entry : readyPutRedisReceiver.entrySet()) {
            String key = entry.getKey();
            if (inRedisValue.get(key) != null) {
                keyValues.put(key, String.valueOf(Integer.parseInt(inRedisValue.get(key)) + 1));
            } else {
                keyValues.put(key, String.valueOf(AustinConstant.TRUE));
            }
        }

        if (CollUtil.isNotEmpty(keyValues)) {
            redisUtil.pipelineSetEx(keyValues, deduplicationTime);
        }
    }
}
