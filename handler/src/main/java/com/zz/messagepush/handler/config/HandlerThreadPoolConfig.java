package com.zz.messagepush.handler.config;

import com.dtp.common.em.QueueTypeEnum;
import com.dtp.common.em.RejectedTypeEnum;
import com.dtp.core.thread.DtpExecutor;
import com.dtp.core.thread.ThreadPoolBuilder;
import com.zz.messagepush.common.constant.ThreadPoolConstant;
import java.util.concurrent.TimeUnit;

/**
 * @Description handler模块 线程池配置
 * @Author 张卫刚
 * @Date Created on 2023/3/21
 */


public class HandlerThreadPoolConfig {


    public static final String PRE_FIX = "austin.";

    /**
     * 业务：处理某个渠道某种消息类型的线程池
     * 配置：不丢弃消息，核心线程数不会随着keepAliveTime时间减少而减少（不会被回收）
     * 动态线程池且被spring管理
     * @return
     */
    public static DtpExecutor getExecutor(String groupId) {
        return ThreadPoolBuilder.newBuilder()
                .threadPoolName(PRE_FIX+groupId)
                .corePoolSize(ThreadPoolConstant.COMMON_CORE_POOL_SIZE)
                .maximumPoolSize(ThreadPoolConstant.COMMON_MAX_POOL_SIZE)
                .allowCoreThreadTimeOut(true)
                .workQueue(QueueTypeEnum.LINKED_BLOCKING_QUEUE.getName(),ThreadPoolConstant.BIG_QUEUE_SIZE,false)
                .rejectedExecutionHandler(RejectedTypeEnum.CALLER_RUNS_POLICY.getName())
                .keepAliveTime(ThreadPoolConstant.COMMON_KEEP_LIVE_TIME)
                .timeUnit(TimeUnit.SECONDS)
                .buildDynamic();
    }

}
