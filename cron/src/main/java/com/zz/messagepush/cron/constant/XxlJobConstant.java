package com.zz.messagepush.cron.constant;

/**
 * @Description  xxl-job常量信息
 * @Author 张卫刚
 * @Date Created on 2023/3/31
 */
public class XxlJobConstant {

    /**
     * 接口路径信息
     */
    public static final String LOGIN_URL = "/login";
    public static final String INSERT_URL = "/jobinfo/add";
    public static final String UPDATE_URL = "/jobinfo/update";
    public static final String DELETE_URL = "/jobinfo/delete";
    public static final String RUN_URL = "/jobinfo/run";
    public static final String STOP_URL = "/jobinfo/stop";

    /**
     * 执行器名称
     */
    public static final String HANDLER_NAME = "austinJobHolder";

    /**
     * 超时时间
     */
    public static final Integer TIME_OUT = 120;

    /**
     * 重试次数
     */
    public static final Integer RETRY_TIME = 2;

}
