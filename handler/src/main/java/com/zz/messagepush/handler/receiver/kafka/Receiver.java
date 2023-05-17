package com.zz.messagepush.handler.receiver.kafka;

import cn.hutool.core.collection.CollUtil;
import com.alibaba.fastjson.JSON;
import com.zz.messagepush.common.domain.dto.TaskInfo;
import com.zz.messagepush.handler.receiver.service.ConsumeService;
import com.zz.messagepush.handler.utils.GroupIdMappingUtils;
import com.zz.messagepush.support.domain.entity.MessageTemplateEntity;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;

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
    private ConsumeService consumeService;

    @KafkaListener(topics = "#{'${austin.kafka.topic.name}'}", containerFactory = "filterContainerFactory")
    public void consumer(ConsumerRecord<?, String> consumerRecord, @Header(KafkaHeaders.GROUP_ID) String topicGroupId) {
        Optional<String> kafkaMessage = Optional.ofNullable(consumerRecord.value());
        if (kafkaMessage.isPresent()) {
            List<TaskInfo> taskInfos = JSON.parseArray(kafkaMessage.get(), TaskInfo.class);
            String groupId = GroupIdMappingUtils.getGroupIdByTaskInfo(CollUtil.getFirst(taskInfos.iterator()));

            //每个消费者只关心自身的信息
            if (groupId.equals(topicGroupId)) {
                consumeService.consume2Send(taskInfos);
            }
            log.info("receive message:{}", JSON.toJSONString(taskInfos));
        }
    }


    @KafkaListener(topics = "#{'${austin.business.recall.topic.name}'}", groupId = "#{'${austin.business.recall.group.name}'}",
            containerFactory = "filterContainerFactory")
    public void recall(ConsumerRecord<?, String> consumerRecord) {
        Optional<String> kafkaMessage = Optional.ofNullable(consumerRecord.value());
        if (kafkaMessage.isPresent()) {
            MessageTemplateEntity messageTemplateEntity = JSON.parseObject(kafkaMessage.get(), MessageTemplateEntity.class);
            consumeService.consume2Recall(messageTemplateEntity);
        }
    }
}
