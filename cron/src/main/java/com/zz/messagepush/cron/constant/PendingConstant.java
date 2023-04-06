package com.zz.messagepush.cron.constant;

/**
 * @Description 缓冲pending常量
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
    public final static Integer THREAD_NUM = 2;

}
