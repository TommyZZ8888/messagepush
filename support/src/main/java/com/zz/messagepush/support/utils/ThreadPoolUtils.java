package com.zz.messagepush.support.utils;

import com.dtp.core.DtpRegistry;
import com.dtp.core.thread.DtpExecutor;
import com.zz.messagepush.support.config.ThreadPoolExecutorShutdownDefinition;
import io.swagger.annotations.Authorization;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @Description 线程池工具类
 * @Author 张卫刚
 * @Date Created on 2023/4/13
 */

@Component
public class ThreadPoolUtils {

    @Autowired
    private ThreadPoolExecutorShutdownDefinition shutdownDefinition;

    private static final String SOURCE_NAME = "austin";


    /**
     * 将当前线程池加入到动态线程池
     * 注册线程池 被spring管理 优雅关闭
     * @param dtpExecutor
     */
    public void registry(DtpExecutor dtpExecutor) {
        DtpRegistry.register(dtpExecutor, SOURCE_NAME);
        shutdownDefinition.registryExecutor(dtpExecutor);
    }

}
