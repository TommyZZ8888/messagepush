package com.zz.messagepush.handler.config;

import cn.hutool.core.thread.ExecutorBuilder;
import com.zz.messagepush.common.utils.MyThreadFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @Description 线程池配置信息
 * @Author 张卫刚
 * @Date Created on 2023/3/21
 */


public class ThreadPoolConfig {

    /**
     * 阻塞队列满了，也不会丢弃任务  CallerRunsPolicy
     * @param coreSize
     * @param maxSize
     * @param queueSize
     * @return
     */
    public static ThreadPoolExecutor getThreadPool(Integer coreSize,Integer maxSize,Integer queueSize) {
        return ExecutorBuilder.create()
                .setCorePoolSize(coreSize)
                .setMaxPoolSize(maxSize)
                .setKeepAliveTime(300L, TimeUnit.SECONDS)
                .setWorkQueue(new LinkedBlockingQueue<>(queueSize))
                .setThreadFactory(MyThreadFactory.create("getThreadPool"))
                .setHandler(new ThreadPoolExecutor.CallerRunsPolicy()).build();
    }

}
