package com.zz.messagepush.common.utils;

import org.jetbrains.annotations.NotNull;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @Description 为了jstack时候能看到哪个线程
 * @Author 张卫刚
 * @Date Created on 2023/3/21
 */
public class MyThreadFactory implements ThreadFactory {

    public static MyThreadFactory create(String namePrefix) {
        return new MyThreadFactory(namePrefix);
    }

    private final AtomicInteger poolNumber = new AtomicInteger(1);

    private final ThreadGroup threadGroup;
    private final AtomicInteger threadNumber = new AtomicInteger(1);

    private final String namePrefix;


    public MyThreadFactory(String namePrefix) {
        threadGroup = Thread.currentThread().getThreadGroup();
        this.namePrefix = namePrefix + " pool " + poolNumber.getAndIncrement() + "-thread-";
    }


    @Override
    public Thread newThread(@NotNull Runnable r) {
        Thread thread = new Thread(threadGroup, r, namePrefix + threadNumber.getAndIncrement(), 0);
        if (thread.isDaemon()) {
            thread.setDaemon(false);
        }
        if (thread.getPriority() != Thread.NORM_PRIORITY) {
            thread.setPriority(Thread.NORM_PRIORITY);
        }
        return thread;
    }
}
