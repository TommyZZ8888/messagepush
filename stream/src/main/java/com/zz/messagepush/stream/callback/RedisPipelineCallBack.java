package com.zz.messagepush.stream.callback;

import io.lettuce.core.RedisFuture;
import io.lettuce.core.api.async.RedisAsyncCommands;

import java.util.List;

/**
 * @Description redis pipeline 接口定义
 * @Author 张卫刚
 * @Date Created on 2023/4/10
 */
public interface RedisPipelineCallBack {

    /**
     * 具体执行逻辑
     * @param redisAsyncCommands
     * @return
     */
    List<RedisFuture<?>> invoke(RedisAsyncCommands redisAsyncCommands);
}
