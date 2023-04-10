package com.zz.messagepush.stream.utils;

import org.apache.flink.api.common.serialization.SimpleStringSchema;
import org.apache.flink.connector.kafka.source.KafkaSource;
import org.apache.flink.connector.kafka.source.enumerator.initializer.OffsetsInitializer;

/**
 * @Description 消息队列 工具类
 * @Author 张卫刚
 * @Date Created on 2023/4/7
 */
public class MessageQueueUtils {

    /**
     * 获取kafka consumer
     * @param topicName
     * @param groupId
     * @return
     */
    public static KafkaSource<String> getKafkaConsumer(String topicName, String groupId,String broken) {
        return KafkaSource.<String>builder()
                .setGroupId(groupId)
                .setTopics(topicName)
                .setBootstrapServers(broken)
                .setValueOnlyDeserializer(new SimpleStringSchema())
                .setStartingOffsets(OffsetsInitializer.earliest()).build();
    }
}
