package com.zz.messagepush.service.api.impl.service;

import com.zz.messagepush.common.domain.TaskInfo;
import com.zz.messagepush.service.api.domain.SendResponse;
import com.zz.messagepush.service.api.domain.dto.BatchSendRequest;
import com.zz.messagepush.service.api.domain.dto.SendRequest;
import com.zz.messagepush.service.api.impl.domain.SendTaskModel;
import com.zz.messagepush.service.api.impl.enums.RequestType;
import com.zz.messagepush.service.api.service.SendService;
import com.zz.messagepush.support.pipeline.ProcessContext;
import com.zz.messagepush.support.pipeline.ProcessHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class SendServiceImpl implements SendService {

    @Autowired
    private ProcessHandler processHandler;

    @Override
    public SendResponse send(SendRequest sendRequest) {
        SendTaskModel sendTaskModel = SendTaskModel.builder()
                .requestType(RequestType.SINGLE.getCode())
                .messageParam(sendRequest.getMessageParam())
                .taskInfo(TaskInfo.builder().messageTemplateId(sendRequest.getMessageTemplateId()).build())
                .build();
        ProcessContext context = ProcessContext.builder()
                .code(sendRequest.getCode())
                .processModel(sendTaskModel).build();
        ProcessContext process = processHandler.process(context);
        return new SendResponse(process.getResponse().getCode(), process.getResponse().getMsg());
    }

    @Override
    public SendResponse batchSend(BatchSendRequest sendRequest) {
        SendTaskModel sendTaskModel = SendTaskModel.builder()
                .requestType(RequestType.BATCH.getCode())
                .messageParamList(sendRequest.getMessageParamList())
                .taskInfo(TaskInfo.builder().messageTemplateId(sendRequest.getMessageTemplateId()).build()).build();

        ProcessContext processContext = ProcessContext.builder()
                .code(sendRequest.getCode())
                .processModel(sendTaskModel).build();

        ProcessContext process = processHandler.process(processContext);

        return new SendResponse(process.getResponse().getCode(), process.getResponse().getMsg());
    }
}
