package com.zz.messagepush.stream.utils;

import org.apache.flink.api.common.serialization.SimpleStringSchema;
import org.apache.flink.connector.kafka.source.KafkaSource;
import org.apache.flink.connector.kafka.source.enumerator.initializer.OffsetsInitializer;
import org.springframework.beans.factory.annotation.Value;

/**
 * @Description flink 工具类
 * @Author 张卫刚
 * @Date Created on 2023/4/7
 */
public class FlinkUtils {

    @Value("${spring.kafka.bootstrap-servers}")
    private String broke;


    /**
     * 获取kafka consumer
     * @param topicName
     * @param groupId
     * @return
     */
    public KafkaSource<String> getKafkaConsumer(String topicName, String groupId) {
        return KafkaSource.<String>builder()
                .setGroupId(groupId)
                .setTopics(topicName)
                .setBootstrapServers(broke)
                .setValueOnlyDeserializer(new SimpleStringSchema())
                .setStartingOffsets(OffsetsInitializer.earliest()).build();
    }
}
