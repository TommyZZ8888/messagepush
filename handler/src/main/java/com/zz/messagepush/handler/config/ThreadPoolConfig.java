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


@Configuration
@EnableAsync
public class ThreadPoolConfig {


    @Bean("smsThreadPool")
    public static ThreadPoolExecutor getSmsThreadPool() {
        return ExecutorBuilder.create()
                .setCorePoolSize(5)
                .setMaxPoolSize(50)
                .setKeepAliveTime(300L, TimeUnit.SECONDS)
                .setWorkQueue(new LinkedBlockingQueue<>(1000))
                .setThreadFactory(MyThreadFactory.create("getSmsThreadPool"))
                .setHandler((r, executor) -> {
                    try {
                        executor.getQueue().put(r);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }).build();
    }


    @Bean("emailThreadPool")
    public static ThreadPoolExecutor getEmailThreadPool() {
        return ExecutorBuilder.create()
                .setCorePoolSize(5)
                .setMaxPoolSize(50)
                .setKeepAliveTime(300L, TimeUnit.SECONDS)
                .setWorkQueue(new LinkedBlockingQueue<>(1000))
                .setThreadFactory(MyThreadFactory.create("getEmailThreadPool"))
                .setHandler((r, executor) -> {
                    try {
                        executor.getQueue().put(r);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }).build();
    }

}
