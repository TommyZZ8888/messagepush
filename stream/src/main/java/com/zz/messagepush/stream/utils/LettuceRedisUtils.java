package com.zz.messagepush.stream.utils;

import com.zz.messagepush.stream.callback.RedisPipelineCallBack;
import com.zz.messagepush.stream.constant.AustinFlinkConstant;
import io.lettuce.core.LettuceFutures;
import io.lettuce.core.RedisClient;
import io.lettuce.core.RedisFuture;
import io.lettuce.core.RedisURI;
import io.lettuce.core.api.StatefulRedisConnection;
import io.lettuce.core.api.async.RedisAsyncCommands;
import io.lettuce.core.codec.ByteArrayCodec;

import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @Description 无环境下使用redis，基于Lettuce封装
 * @Author 张卫刚
 * @Date Created on 2023/4/10
 */
public class LettuceRedisUtils {
    /**
     * 初始化redisClient
     */

    private static RedisClient redisClient;

    static {
        RedisURI redisURI = RedisURI.builder().withHost(AustinFlinkConstant.REDIS_IP)
                .withPort(Integer.parseInt(AustinFlinkConstant.REDIS_PORT))
                .withPassword(AustinFlinkConstant.PASSWORD.toCharArray())
                .build();
        redisClient = RedisClient.create(redisURI);
    }


    /**
     * 封装pipeline操作
     *
     * @param pipelineCallBack
     */
    public static void pipeline(RedisPipelineCallBack pipelineCallBack) {
        StatefulRedisConnection<byte[], byte[]> connect = redisClient.connect(new ByteArrayCodec());
        RedisAsyncCommands<byte[], byte[]> commands = connect.async();
        List<RedisFuture<?>> futures = pipelineCallBack.invoke(commands);

        commands.flushCommands();
        LettuceFutures.awaitAll(10, TimeUnit.SECONDS, futures.toArray(new RedisFuture[0]));
        connect.close();
    }

}
