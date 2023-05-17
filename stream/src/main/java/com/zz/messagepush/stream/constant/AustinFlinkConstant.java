package com.zz.messagepush.stream.constant;

/**
 * @Description flink常量信息
 * @Author 张卫刚
 * @Date Created on 2023/4/10
 */
public class AustinFlinkConstant {

    /**
     * kafka配置信息
     * 如果想要自己监听到所有信息，改掉groupId
     */
    public static final String GROUP_ID = "austinLogGroup";
    public static final String TOPIC_NAME="austinTraceLog";
    public static final String BROKEN=  "austin.kafka";

    /**
     * redis配置信息
     */
    public static final String REDIS_IP = "172.16.16.105";
    public static final String REDIS_PORT = "6379";
    public static final String PASSWORD = "123456";

    /**
     * flink流程常量
     */
    public static final String SOURCE_NAME = "austin_kafka_source";
    public static final String FUNCTION_NAME = "austin_transfer";
    public static final String SINK_NAME = "austin_sink";
    public static final String JOB_NAME = "AustinBootStrap";
}
