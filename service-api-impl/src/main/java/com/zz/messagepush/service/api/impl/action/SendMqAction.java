package com.zz.messagepush.service.api.impl.action;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.nacos.shaded.com.google.common.base.Throwables;
import com.zz.messagepush.common.domain.ResponseResult;
import com.zz.messagepush.common.enums.RespStatusEnum;
import com.zz.messagepush.service.api.enums.BusinessCode;
import com.zz.messagepush.service.api.impl.domain.SendTaskModel;
import com.zz.messagepush.support.mq.SendMqService;
import com.zz.messagepush.support.pipeline.BusinessProcess;
import com.zz.messagepush.support.pipeline.ProcessContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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
    private SendMqService sendMqService;

    @Value("${austin.business.topic.name}")
    private String sendMessageTopic;

    @Value("${austin.business.recall.topic.name}")
    private String austinRecall;

    @Value("${austin.business.tagId.value}")
    private String tagId;

    @Override
    public void process(ProcessContext<SendTaskModel> context) {
        SendTaskModel processModel = context.getProcessModel();

        try {
            if (BusinessCode.COMMON_SEND.getCode().equals(context.getCode())) {
                String message = JSON.toJSONString(processModel.getTaskInfo(), SerializerFeature.WriteClassName);
                sendMqService.send(sendMessageTopic, message, tagId);
            } else if (BusinessCode.RECALL_SEND.getCode().equals(context.getCode())) {
                String message = JSON.toJSONString(processModel.getMessageTemplateEntity(), SerializerFeature.WriteClassName);
                sendMqService.send(austinRecall, message, tagId);
            }
        } catch (Exception e) {
            context.setNeedBreak(true).setResponse(ResponseResult.fail(RespStatusEnum.SERVICE_ERROR.getDescription()));
            log.error("send kafka fail! e:{}", Throwables.getStackTraceAsString(e));
        }
    }
}
