package com.zz.messagepush.support.utils;

import org.apache.commons.lang3.StringUtils;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.header.internals.RecordHeader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * @Description KafkaUtil
 * @Author 张卫刚
 * @Date Created on 2023/5/16
 */

@Component
public class KafkaUtil {

    @Autowired
    private KafkaTemplate kafkaTemplate;

    @Value("${austin.business.tagId.key}")
    private String tagIdKey;


    public void send(String topicName, String jsonMessage) {
        send(topicName, jsonMessage, null);
    }


    /**
     * 发送kafka消息
     * 支持tag过滤
     *
     * @param topicName
     * @param jsonMessage
     * @param tagIdKey
     */
    public void send(String topicName, String jsonMessage, String tagIdKey) {
        if (StringUtils.isNotBlank(tagIdKey)) {
            List<RecordHeader> recordHeaders = List.of(new RecordHeader(tagIdKey, tagIdKey.getBytes(StandardCharsets.UTF_8)));
            kafkaTemplate.send(new ProducerRecord(topicName, null, null, null, jsonMessage, recordHeaders));
        } else {
            kafkaTemplate.send(topicName, jsonMessage);
        }
    }
}
