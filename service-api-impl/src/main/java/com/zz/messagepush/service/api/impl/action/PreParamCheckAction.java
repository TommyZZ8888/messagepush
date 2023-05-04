package com.zz.messagepush.service.api.impl.action;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.zz.messagepush.common.domain.ResponseResult;
import com.zz.messagepush.common.enums.RespStatusEnum;
import com.zz.messagepush.service.api.domain.dto.MessageParam;
import com.zz.messagepush.service.api.impl.domain.SendTaskModel;
import com.zz.messagepush.support.pipeline.BusinessProcess;
import com.zz.messagepush.support.pipeline.ProcessContext;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @Description 参数校验
 * @Author 张卫刚
 * @Date Created on 2023/3/16
 */

@Service
public class PreParamCheckAction implements BusinessProcess<SendTaskModel> {

    private static final Integer BATCH_RECEIVER_SIZE = 100;

    @Override
    public void process(ProcessContext<SendTaskModel> context) {
        SendTaskModel processModel = context.getProcessModel();

        Long messageTemplateId = processModel.getMessageTemplateId();
        List<MessageParam> messageParamList = processModel.getMessageParamList();

        //没有传入消息模板id或者messageParam
        if (messageTemplateId == null || CollUtil.isEmpty(messageParamList)) {
            context.setNeedBreak(true).setResponse(ResponseResult.fail(RespStatusEnum.CLIENT_BAD_PARAMETERS.getDescription()));
            return;
        }

        //过滤掉接收者为null的messageParam
        List<MessageParam> list = messageParamList.stream().filter(receiver -> !StrUtil.isBlank(receiver.getReceiver())).collect(Collectors.toList());
        if (CollUtil.isEmpty(list)) {
            context.setNeedBreak(true).setResponse(ResponseResult.fail(RespStatusEnum.CLIENT_BAD_PARAMETERS.getDescription()));
            return;
        }
        processModel.setMessageParamList(list);
        if (list.stream().anyMatch(messageParam -> messageParam.getReceiver().split(StrUtil.COMMA).length >= BATCH_RECEIVER_SIZE)) {
            context.setNeedBreak(true).setResponse(ResponseResult.fail(RespStatusEnum.TOO_MANY_RECEIVER.getDescription()));
        }
    }
}
