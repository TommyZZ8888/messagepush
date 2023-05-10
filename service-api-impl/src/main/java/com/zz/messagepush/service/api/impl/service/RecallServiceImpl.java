package com.zz.messagepush.service.api.impl.service;

import com.zz.messagepush.common.domain.ResponseResult;
import com.zz.messagepush.service.api.domain.SendResponse;
import com.zz.messagepush.service.api.domain.dto.SendRequest;
import com.zz.messagepush.service.api.impl.domain.SendTaskModel;
import com.zz.messagepush.service.api.service.RecallService;
import com.zz.messagepush.support.pipeline.ProcessContext;
import com.zz.messagepush.support.pipeline.ProcessHandler;
import com.zz.messagepush.support.pipeline.ProcessModel;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @Description RecallServiceImpl
 * @Author 张卫刚
 * @Date Created on 2023/5/10
 */
public class RecallServiceImpl implements RecallService {

    @Autowired
    private ProcessHandler processHandler;

    @Override
    public SendResponse recall(SendRequest sendRequest) {
        SendTaskModel sendTaskModel = SendTaskModel.builder().messageTemplateId(sendRequest.getMessageTemplateId()).build();
        ProcessContext<ProcessModel> context = ProcessContext.builder()
                .code(sendRequest.getCode())
                .processModel(sendTaskModel)
                .needBreak(false)
                .response(ResponseResult.success()).build();
        ProcessContext process = processHandler.process(context);
        return new SendResponse(process.getResponse().getCode(),process.getResponse().getMsg());
    }
}
