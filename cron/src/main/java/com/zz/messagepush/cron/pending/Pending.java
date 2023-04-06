package com.zz.messagepush.cron.pending;

import cn.hutool.core.collection.CollUtil;
import com.alibaba.nacos.shaded.com.google.common.base.Throwables;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.BlockingQueue;

/**
 * @Description 阻塞队列-消费者生产者-实现
 * @Author 张卫刚
 * @Date Created on 2023/4/6
 */

@Slf4j
@Data
public abstract class Pending<T> {

    /**
     * 可用的线程数
     */
    public final static Integer DEFAULT_THREAD_NUM = Runtime.getRuntime().availableProcessors();

    /**
     * 阻塞队列
     */
    protected BlockingQueue<T> blockingQueue;

    /**
     * 消费的线程数
     */
    protected Integer threadNum = DEFAULT_THREAD_NUM;

    /**
     * 将元素放入阻塞队列
     *
     * @param t
     */
    public void pending(T t) {
        try {
            blockingQueue.put(t);
        } catch (InterruptedException e) {
            log.error("Pending#pending error:{}", Throwables.getStackTraceAsString(e));
        }
    }

    /**
     * 消费阻塞队列中的元素
     * @param list
     */
    public void handle(List<T> list) {
        if (CollUtil.isEmpty(list)) {
            return;
        }
        try {
            doHandle(list);
        } catch (Exception e) {
            log.error("Pending#pending fail:{}", Throwables.getStackTraceAsString(e));
        }
    }


    /**
     * 初始化并启动
     * @param pendingParam
     */
    public abstract void initAndStart(PendingParam<T> pendingParam);

    /**
     * 处理阻塞队列里的元素 真正方法
     *
     * @param list
     */
    public abstract void doHandle(List<T> list);
}
