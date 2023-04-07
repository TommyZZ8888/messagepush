package com.zz.messagepush.cron.pending;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.thread.ThreadUtil;
import com.alibaba.nacos.shaded.com.google.common.base.Throwables;
import com.zz.messagepush.cron.constant.PendingConstant;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

/**
 * @Description 阻塞队列-消费者生产者-实现
 * @Author 张卫刚
 * @Date Created on 2023/4/6
 */

@Slf4j
@Data
public abstract class AbstractLazyPending<T> {

    /**
     * 子类构造方法必须初始化该参数
     */
    protected PendingParam<T> pendingParam;

    /**
     * 批量装载任务
     */
    private List<T> tasks = new ArrayList<>();
    /**
     * 上次执行的时间
     */
    private Long lastHoldTime = System.currentTimeMillis();

    @PostConstruct
    public void initConsumePending() {
        ThreadUtil.newSingleExecutor().execute(() -> {
            while (true) {
                try {
                    T poll = pendingParam.getBlockingQueue().poll(pendingParam.getThresholdTime(), TimeUnit.MILLISECONDS);
                    if (poll != null) {
                        tasks.add(poll);
                    }
                    //处理条件: 1.数量超限  2.时间超限
                    if (CollUtil.isNotEmpty(tasks) && dataReady()) {
                        List<T> taskList = tasks;
                        tasks = new ArrayList<>();
                        lastHoldTime = System.currentTimeMillis();

                        pendingParam.getExecutorService().execute(() -> this.handle(taskList));
                    }
                } catch (Exception e) {
                    log.error("BatchPendingThread#pending fail:{}", Throwables.getStackTraceAsString(e));
                }
            }
        });
    }


    /**
     * 1. 数量超限
     * 2. 时间超限
     *
     * @return
     */
    private boolean dataReady() {
        return tasks.size() >= pendingParam.getThresholdNum() ||
                (System.currentTimeMillis() - lastHoldTime >= pendingParam.getThresholdTime());
    }


    /**
     * 将元素放入阻塞队列
     *
     * @param t
     */
    public void pending(T t) {
        try {
            pendingParam.getBlockingQueue().put(t);
        } catch (InterruptedException e) {
            log.error("Pending#pending error:{}", Throwables.getStackTraceAsString(e));
        }
    }

    /**
     * 消费阻塞队列中的元素
     *
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
     * 处理阻塞队列里的元素 真正方法
     *
     * @param list
     */
    public abstract void doHandle(List<T> list);
}
