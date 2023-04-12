package com.zz.messagepush.cron.constant;

import org.apache.flink.client.program.PackagedProgramUtils;
import org.checkerframework.checker.index.qual.PolyUpperBound;

import java.util.concurrent.BlockingDeque;
import java.util.concurrent.LinkedBlockingDeque;

/**
 * @Description 延迟缓冲 pending 常量信息
 * @Author 张卫刚
 * @Date Created on 2023/4/6
 */
public class PendingConstant {

    /**
     * 阻塞队列大小
     */
    public final static Integer QUEUE_SIZE = 100;

    /**
     * 触发执行的数量阈值
     */
    public final static Integer NUM_THRESHOLD = 100;

    /**
     * 触发执行的时间阈值
     */
    public final static Long TIME_THRESHOLD = 100L;

    /**
     * 消费线程数
     */
    public final static Integer CORE_POOL_SIZE = 5;

    public final static Integer MAX_POOL_SIZE = 5;

    public final static Integer KEEP_LIVE_TIME = 20;

    public final static BlockingDeque BLOCKING_QUEUE = new LinkedBlockingDeque(5);

}
