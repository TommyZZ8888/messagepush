package com.zz.messagepush.cron.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import javax.annotation.PostConstruct;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * @Description
 * @Author 张卫刚
 * @Date Created on 2023/4/11
 */

@Data
@ConfigurationProperties(prefix = "austin.async.task")
public class AsyncExecutionProperties {

    private String threadNamePrefix;

    private Integer maxSize;

    private Integer coreSize;

    private int queueCapacity;

    private int keepAlive;

    private RejectedEnum rejectedExecutionHandler = RejectedEnum.CALLRUNSPOLICY;

    private boolean allowCoreThreadTimeout;

    private int awaitTerminationSeconds;

    private boolean waitForTasksToCompleteOnShutDown;

    @PostConstruct
    void init() {
        if (coreSize <= 0) {
            this.coreSize = Runtime.getRuntime().availableProcessors();
        }
        if (maxSize <= 0) {
            this.maxSize = coreSize << 1;
        }
    }

    public enum RejectedEnum {
        /**
         * 直接抛出异常
         */
        ABORTPOLICY(new ThreadPoolExecutor.AbortPolicy()),
        /**
         * 交个当前run_thread 运行
         */
        CALLRUNSPOLICY(new ThreadPoolExecutor.CallerRunsPolicy()),
        /***
         * 直接丢掉
         */
        DISCARDPOLICY(new ThreadPoolExecutor.DiscardPolicy()),
        /**
         * 丢掉队列中排队时间最久的任务
         */
        DISCARDOLDESTPOLICY(new ThreadPoolExecutor.DiscardOldestPolicy());
        /**
         * 线程池默认拒绝策略
         */
        private RejectedExecutionHandler handler;

        RejectedEnum(RejectedExecutionHandler handler) {
            this.handler = handler;
        }


        public RejectedExecutionHandler getHandler() {
            return handler;
        }

        public void setHandler(RejectedExecutionHandler handler) {
            this.handler = handler;
        }
    }
}
