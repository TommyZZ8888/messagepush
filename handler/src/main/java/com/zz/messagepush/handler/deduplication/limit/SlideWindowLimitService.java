package com.zz.messagepush.handler.deduplication.limit;

import cn.hutool.core.util.IdUtil;
import com.zz.messagepush.common.domain.dto.TaskInfo;
import com.zz.messagepush.handler.deduplication.AbstractDeduplicationService;
import com.zz.messagepush.handler.domain.DeduplicationParam;
import com.zz.messagepush.support.utils.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.scripting.support.ResourceScriptSource;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @Description 滑动窗口去重器（内容去重采用redis的zset滑动窗口去重，可以做到严格控制单位时间内的频次）
 * @Author 张卫刚
 * @Date Created on 2023/4/19
 */

@Service("SlideWindowLimitService")
public class SlideWindowLimitService extends AbstractLimitService {

    private static final String LIMIT_TAG = "SW_";

    @Autowired
    private RedisUtil redisUtil;


    private DefaultRedisScript<Long> redisScript;

    @PostConstruct
    public void init() {
        redisScript = new DefaultRedisScript<>();
        redisScript.setResultType(Long.class);
        redisScript.setScriptSource(new ResourceScriptSource(new ClassPathResource("limit.lua")));
    }

    /**
     *
     * @param deduplicationService 去重器对象
     * @param taskInfo
     * @param param 去重参数
     * @return 返回不符合条件的手机号码
     */
    @Override
    public Set<String> limitFilter(AbstractDeduplicationService deduplicationService, TaskInfo taskInfo, DeduplicationParam param) {
        Set<String> filterReceiver = new HashSet<>(taskInfo.getReceiver());
        long nowTime = System.currentTimeMillis();
        for (String receive : taskInfo.getReceiver()) {
            String key = LIMIT_TAG + deduplicationSingleKey(deduplicationService, taskInfo, receive);
            String sourceValue = String.valueOf(IdUtil.getSnowflake().nextId());
            String source = String.valueOf(nowTime);
            if (redisUtil.execLimitLua(redisScript, List.of(key), String.valueOf(param.getDeduplicationTime() * 1000), source, String.valueOf(param.getCountNum()), sourceValue)) {
                filterReceiver.add(receive);
            }
        }
        return filterReceiver;
    }
}
