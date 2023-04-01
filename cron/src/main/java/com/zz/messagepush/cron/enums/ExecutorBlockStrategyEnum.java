package com.zz.messagepush.cron.enums;

/**
 * @Description 执行阻塞队列
 * @Author 张卫刚
 * @Date Created on 2023/3/31
 */
public enum ExecutorBlockStrategyEnum {

    /**
     * 单机串行
     */
    SERIAL_EXECUTION,

    /**
     * 丢弃后续调度
     */
    DISCARD_LATER,

    /**
     * 覆盖之前执行
     */
    COVER_EARLY;

    ExecutorBlockStrategyEnum(){}
}
