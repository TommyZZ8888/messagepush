package com.zz.messagepush.common.constant;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * @Description 线程池常见的常量信息
 * @Author 张卫刚
 * @Date Created on 2023/4/13
 */
public class ThreadPoolConstant {

    public static final Integer SINGLE_CORE_POOL_SIZE = 2;
    public static final Integer SINGLE_MAX_POOL_SIZE = 2;
    public static final Integer SMALL_KEEP_LIVE_TIME = 30;

    public static final Integer COMMON_CORE_POOL_SIZE = 5;
    public static final Integer COMMON_MAX_POOL_SIZE = 5;
    public static final Integer COMMON_KEEP_LIVE_TIME = 50;
    public static final Integer COMMON_QUEUE_SIZE = 20;

    public static final Integer BIG_QUEUE_SIZE = 1024;
    public static final BlockingQueue BIG_BLOCKING_QUEUE = new LinkedBlockingQueue(BIG_QUEUE_SIZE);


}
