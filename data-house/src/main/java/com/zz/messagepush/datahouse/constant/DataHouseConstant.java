package com.zz.messagepush.datahouse.constant;

/**
 * @Description DataHouseConstant
 * @Author 张卫刚
 * @Date Created on 2023/6/21
 */
public class DataHouseConstant {

    /**
     * catalog name
     */
    public static final String CATALOG_NAME = "my_hive";

    /**
     * 库名
     */
    public static final String CATALOG_DEFAULT_DATABASE = "austin";

    /**
     * 消费Kafka消息，写入的表
     */
    public static final String KAFKA_SOURCE_TABLE_NAME = "anchor_log_source";

    /**
     * 最终落到hive的表
     */
    public static final String KAFKA_SINK_TABLE_NAME = "message_anchor";

    /**
     * 源Kafka topic
     */
    public static final String KAFKA_TOPIC = "austinTraceLog";


    /**
     * eg:  127.0.0.1:9092
     * 消费Kafka的ip和端口
     */
    public static final String KAFKA_IP_PORT = "127.0.0.1:9092";

}
