package com.zz.messagepush.handler.receiver.kafka;

import cn.hutool.core.collection.CollUtil;
import com.alibaba.fastjson.JSON;
import com.zz.messagepush.common.domain.AnchorInfo;
import com.zz.messagepush.common.domain.LogParam;
import com.zz.messagepush.common.domain.dto.TaskInfo;
import com.zz.messagepush.common.enums.AnchorStateEnum;
import com.zz.messagepush.handler.pending.Task;
import com.zz.messagepush.handler.pending.TaskPendingHolder;
import com.zz.messagepush.handler.utils.GroupIdMappingUtils;
import com.zz.messagepush.support.utils.LogUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
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


    private static final String LOG_BIZ_TYPE  = "Receiver#consumer";

    @Autowired
    private ApplicationContext context;

    @Autowired
    private TaskPendingHolder taskPendingHolder;

    @KafkaListener(topics = "#{'${austin.kafka.topic.name}'}")
    public void consumer(ConsumerRecord<?, String> consumerRecord, @Header(KafkaHeaders.GROUP_ID) String topicGroupId) {
        Optional<String> kafkaMessage = Optional.ofNullable(consumerRecord.value());
        if (kafkaMessage.isPresent()) {
            List<TaskInfo> taskInfos = JSON.parseArray(kafkaMessage.get(), TaskInfo.class);
            String groupId = GroupIdMappingUtils.getGroupIdByTaskInfo(CollUtil.getFirst(taskInfos.iterator()));

            //每个消费者只关心自身的信息
            if (groupId.equals(topicGroupId)){
                for (TaskInfo taskInfo : taskInfos) {
                    LogUtils.print(LogParam.builder().bizType(LOG_BIZ_TYPE).object(taskInfo).build(), AnchorInfo.builder().ids(taskInfo.getReceiver()).businessId(taskInfo.getBusinessId()).state(AnchorStateEnum.RECEIVE.getCode()).build());
                    Task task = context.getBean(Task.class).setTaskInfo(taskInfo);
                    taskPendingHolder.route(topicGroupId).execute(task);
                }
            }
            log.info("receive message:{}", JSON.toJSONString(taskInfos));
        }
    }
}
