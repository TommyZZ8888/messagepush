package com.zz.messagepush.service.api.impl.action;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.nacos.shaded.com.google.common.base.Throwables;
import com.zz.messagepush.common.domain.ResponseResult;
import com.zz.messagepush.common.enums.RespStatusEnum;
import com.zz.messagepush.service.api.impl.domain.SendTaskModel;
import com.zz.messagepush.support.pipeline.BusinessProcess;
import com.zz.messagepush.support.pipeline.ProcessContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

/**
 * @Description 参数校验
 * @Author 张卫刚
 * @Date Created on 2023/3/16
 */

@Slf4j
@Service
public class SendMqAction implements BusinessProcess<SendTaskModel> {

    @Autowired
    private KafkaTemplate kafkaTemplate;

    @Value("${austin.rabbitmq.topic.name}")
    private String topicName;

    @Override
    public void process(ProcessContext<SendTaskModel> context) {
        SendTaskModel processModel = context.getProcessModel();

        try {
            kafkaTemplate.send(topicName, JSON.toJSONString(processModel.getTaskInfo(), SerializerFeature.WriteClassName));
        } catch (Exception e) {
            context.setNeedBreak(true).setResponse(ResponseResult.fail(RespStatusEnum.SERVICE_ERROR.getDescription()));
            log.error("send kafka fail! e:{}", Throwables.getStackTraceAsString(e));
        }
    }
}
