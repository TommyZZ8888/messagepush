package com.zz.messagepush.cron.enums;

/**
 * @Description 路由策略
 * @Author 张卫刚
 * @Date Created on 2023/3/31
 */
public enum ExecutorRouteStrategyEnum {

    FIRST,
    LAST,
    ROUND,
    RANDOM,
    CONSISTENT_HASH,
    LEAST_FREQUENTLY_USED,
    LEAST_RECENTLY_USED,
    FAILOVER,
    BUSYOVER,
    SHARDING_BROADCAST;

    ExecutorRouteStrategyEnum() {
    }
}
