package com.zz.messagepush.cron.config;

import com.alibaba.nacos.shaded.com.google.common.base.Throwables;
import lombok.extern.slf4j.Slf4j;
import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

/**
 * @Description 处理定时任务的线程池配置信息 为@Async注解服务
 * @Author 张卫刚
 * @Date Created on 2023/4/11
 */
@EnableAsync
@Configuration
@Slf4j
@EnableConfigurationProperties(AsyncExecutionProperties.class)
public class AsyncConfiguration implements AsyncConfigurer {

    @Bean("austinExecutor")
    public ThreadPoolTaskExecutor executor(AsyncExecutionProperties properties) {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(properties.getCoreSize());
        executor.setMaxPoolSize(properties.getMaxSize());
        executor.setKeepAliveSeconds(properties.getKeepAlive());
        executor.setQueueCapacity(properties.getQueueCapacity());
        executor.setThreadNamePrefix(properties.getThreadNamePrefix());
        executor.setRejectedExecutionHandler(properties.getRejectedExecutionHandler().getHandler());
        executor.setAllowCoreThreadTimeOut(properties.isAllowCoreThreadTimeout());
        executor.setWaitForTasksToCompleteOnShutdown(properties.isWaitForTasksToCompleteOnShutDown());
        executor.setAwaitTerminationSeconds(properties.getAwaitTerminationSeconds());
        log.info("austinExecutor: {} ", executor);
        executor.initialize();
        return executor;
    }

    @Override
    public AsyncUncaughtExceptionHandler getAsyncUncaughtExceptionHandler() {
        return (ex, method, params) -> log.error("austinExecutor execute fail!method:{},params:{},ex:{}", method, params, Throwables.getStackTraceAsString(ex));
    }
}
