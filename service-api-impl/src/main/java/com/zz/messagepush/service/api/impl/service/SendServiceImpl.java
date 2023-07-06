package com.zz.messagepush.service.api.impl.service;

import cn.monitor4all.logRecord.annotation.OperationLog;
import com.zz.messagepush.common.domain.ResponseResult;
import com.zz.messagepush.common.enums.RespStatusEnum;
import com.zz.messagepush.service.api.domain.SendResponse;
import com.zz.messagepush.service.api.domain.dto.BatchSendRequest;
import com.zz.messagepush.service.api.domain.dto.SendRequest;
import com.zz.messagepush.service.api.impl.domain.SendTaskModel;
import com.zz.messagepush.service.api.service.SendService;
import com.zz.messagepush.support.pipeline.ProcessContext;
import com.zz.messagepush.support.pipeline.ProcessHandler;
import com.zz.messagepush.support.pipeline.ProcessModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.Collections;


/**
 * @author DELL
 */
@Service
public class SendServiceImpl implements SendService {

    @Autowired
    private ProcessHandler processHandler;

    @Override
    @OperationLog(bizType = "SendService#send", bizId = "#sendRequest.messageTemplateId", msg = "#sendRequest")
    public SendResponse send(SendRequest sendRequest) {
        // 添加对 sendRequest 参数的判空,防止后面空指针
        if(ObjectUtils.isEmpty(sendRequest)){
            return new SendResponse(RespStatusEnum.CLIENT_BAD_PARAMETERS.getCode(), RespStatusEnum.CLIENT_BAD_PARAMETERS.getDescription());
        }

        SendTaskModel sendTaskModel = SendTaskModel.builder()
                .messageTemplateId(sendRequest.getMessageTemplateId())
                .messageParamList(Collections.singletonList(sendRequest.getMessageParam()))
                .build();
        ProcessContext context = ProcessContext.builder()
                .code(sendRequest.getCode())
                .processModel(sendTaskModel)
                .needBreak(false)
                .response(ResponseResult.success()).build();
        ProcessContext process = processHandler.process(context);
        return new SendResponse(process.getResponse().getCode(), process.getResponse().getMsg());
    }

    @Override
    @OperationLog(bizType = "SendService#batchSend", bizId = "#batchSendRequest.messageTemplateId", msg = "#batchSEndRequest")
    public SendResponse batchSend(BatchSendRequest sendRequest) {
        SendTaskModel sendTaskModel = SendTaskModel.builder()
                .messageTemplateId(sendRequest.getMessageTemplateId())
                .messageParamList(sendRequest.getMessageParamList()).build();

        ProcessContext processContext = ProcessContext.builder()
                .code(sendRequest.getCode())
                .processModel(sendTaskModel)
                .needBreak(false)
                .response(ResponseResult.success()).build();

        ProcessContext process = processHandler.process(processContext);

        return new SendResponse(process.getResponse().getCode(), process.getResponse().getMsg());
    }
}
