package com.zz.messagepush.handler.receiver.kafka;

import com.alibaba.fastjson.JSON;
import com.zz.messagepush.common.domain.dto.SmsContentModel;
import com.zz.messagepush.common.domain.dto.TaskInfo;
import com.zz.messagepush.handler.handler.impl.SmsHandler;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import javax.crypto.spec.OAEPParameterSpec;
import java.util.List;
import java.util.Optional;

/**
 * @Description kafka消费mq的消息
 * @Author 张卫刚
 * @Date Created on 2023/3/16
 */

@Component
@Slf4j
public class Receiver {

    @Autowired
    private SmsHandler smsHandler;

    @KafkaListener(topics = "#{'${austin.kafka.topic.name}'}", groupId = "austin")
    public void consumer(ConsumerRecord<?, String> consumerRecord) {
        Optional<String> kafkaMessage = Optional.ofNullable(consumerRecord.value());
        if (kafkaMessage.isPresent()) {
            List<TaskInfo> taskInfos = JSON.parseArray(kafkaMessage.get(), TaskInfo.class);
            for (TaskInfo task : taskInfos) {
                smsHandler.doHandler(task);
            }
            log.info("receive message:{}", JSON.toJSONString(taskInfos));
        }
    }

}
