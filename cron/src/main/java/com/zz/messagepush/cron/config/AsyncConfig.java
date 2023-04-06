package com.zz.messagepush.cron.config;

import lombok.extern.slf4j.Slf4j;
import org.checkerframework.framework.qual.ConditionalPostconditionAnnotation;
import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;

import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * @Description 处理定时任务的线程池配置，为@Async注解服务
 * @Author 张卫刚
 * @Date Created on 2023/4/6
 */

@EnableAsync
@Configuration
@Slf4j
public class AsyncConfig implements AsyncConfigurer {

    @Bean("austinExecutor")
    public ThreadPoolTaskExecutor executor() {
        ThreadPoolTaskExecutor threadPoolExecutor = new ThreadPoolTaskExecutor();
        threadPoolExecutor.setCorePoolSize(5);
        threadPoolExecutor.setMaxPoolSize(50);
        threadPoolExecutor.setQueueCapacity(1000);
        threadPoolExecutor.setKeepAliveSeconds(300);
        threadPoolExecutor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        threadPoolExecutor.setThreadNamePrefix("austin ExecutorAsync+");
        threadPoolExecutor.setWaitForTasksToCompleteOnShutdown(true);
        threadPoolExecutor.setAwaitTerminationSeconds(30);
        threadPoolExecutor.initialize();
        return threadPoolExecutor;
    }

    @Override
    public Executor getAsyncExecutor() {
        return executor();
    }

    @Override
    public AsyncUncaughtExceptionHandler getAsyncUncaughtExceptionHandler() {
        return AsyncConfigurer.super.getAsyncUncaughtExceptionHandler();
    }
}
