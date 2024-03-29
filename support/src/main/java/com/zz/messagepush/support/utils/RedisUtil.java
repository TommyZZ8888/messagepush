package com.zz.messagepush.support.utils;

import cn.hutool.core.collection.CollUtil;
import com.google.common.base.Throwables;
import com.zz.messagepush.common.constant.AustinConstant;
import com.zz.messagepush.common.constant.CommonConstant;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * @Description 对redis的某些操作的二次封装
 * @Author 张卫刚
 * @Date Created on 2023/3/23
 */
@Component
@Slf4j
public class RedisUtil {

    @Autowired
    private StringRedisTemplate redisTemplate;


    /**
     * mGet将结果封装成map
     *
     * @param keys
     * @return
     */
    public Map<String, String> mGet(List<String> keys) {
        Map<String, String> result = new HashMap<>(keys.size());
        try {
            List<String> value = redisTemplate.opsForValue().multiGet(keys);
            if (CollUtil.isNotEmpty(value)) {
                for (int i = 0; i < keys.size(); i++) {
                    result.put(keys.get(i), value.get(i));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.error("redis mGet fail! e:{}", Throwables.getStackTraceAsString(e));
        }
        return result;
    }


    /**
     * hGetAll
     *
     * @param key
     * @return
     */
    public Map<Object, Object> hGetAll(String key) {
        try {
            Map<Object, Object> entries = redisTemplate.opsForHash().entries(key);
            return entries;
        } catch (Exception e) {
            log.error("redis hGetAll fail:{}", Throwables.getStackTraceAsString(e));
        }
        return null;
    }

    /**
     * range
     *
     * @param key
     * @param start
     * @param end
     * @return
     */
    public List<String> lRange(String key, Long start, Long end) {
        try {
            return redisTemplate.opsForList().range(key, start, end);
        } catch (Exception e) {
            log.error("RedisUtils#lRange fail! e:{}", Throwables.getStackTraceAsString(e));
        }
        return null;
    }


    /**
     * lpush并设置过期时间
     *
     * @param key
     * @param value
     * @param second
     */
    public void lPush(String key, String value, Long second) {
        try {
            redisTemplate.executePipelined((RedisCallback<?>) connection -> {
                connection.lPush(key.getBytes(), value.getBytes());
                connection.expire(key.getBytes(), second);
                return null;
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public Long lLen(String key) {
        try {
            return redisTemplate.opsForList().size(key);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0L;
    }

    public String lPop(String key) {
        try {
            return redisTemplate.opsForList().leftPop(key);
        } catch (Exception e) {
            log.error("RedisUtils#pipelineSetEx fail! e:{}", Throwables.getStackTraceAsString(e));
        }
        return "";
    }


    /**
     * pipeline 设置key-value并设置过期时间
     *
     * @param keyValues
     * @param seconds
     */
    public void pipelineSetEx(Map<String, String> keyValues, Long seconds) {
        try {
            redisTemplate.executePipelined((RedisCallback<?>) connection -> {
                for (Map.Entry<String, String> entry : keyValues.entrySet()) {
                    connection.setEx(entry.getKey().getBytes(), seconds, entry.getValue().getBytes());
                }
                return null;
            });
        } catch (Exception e) {
            log.error("redis pipelineSetEX fail! e:{}", Throwables.getStackTraceAsString(e));
        }
    }

    /**
     * pipeline设置keyvalue 并设置过期时间
     *
     * @param keyValues
     * @param seconds   过期时间
     * @param delta     自增的步长
     */
    public void pipelineHashIncrByEx(Map<String, String> keyValues, Long seconds, Long delta) {
        try {
            redisTemplate.executePipelined((RedisCallback<String>) connection -> {
                for (Map.Entry<String, String> entry : keyValues.entrySet()) {
                    connection.hIncrBy(entry.getKey().getBytes(), entry.getValue().getBytes(), delta);
                    connection.expire(entry.getKey().getBytes(), seconds);
                }
                return null;
            });
        } catch (Exception e) {
            log.error("redis pipelineHashIncr fail :{}", Throwables.getStackTraceAsString(e));
        }
    }


    public Boolean execLimitLua(RedisScript<Long> redisScript, List<String> keys, String... args) {
        try {
            Long execute = redisTemplate.execute(redisScript, keys, args);
            if (Objects.isNull(execute)) {
                return false;
            }
            return CommonConstant.TRUE.equals(execute.intValue());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

}
