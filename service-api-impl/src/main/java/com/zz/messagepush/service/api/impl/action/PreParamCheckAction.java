package com.zz.messagepush.service.api.impl.action;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.zz.messagepush.common.domain.ResponseResult;
import com.zz.messagepush.common.enums.RespStatusEnum;
import com.zz.messagepush.service.api.domain.dto.MessageParam;
import com.zz.messagepush.service.api.impl.domain.SendTaskModel;
import com.zz.messagepush.support.pipeline.BusinessProcess;
import com.zz.messagepush.support.pipeline.ProcessContext;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @Description 参数校验
 * @Author 张卫刚
 * @Date Created on 2023/3/16
 */
public class PreParamCheckAction implements BusinessProcess {
    @Override
    public void process(ProcessContext context) {
        SendTaskModel processModel = (SendTaskModel) context.getProcessModel();

        Long messageTemplateId = processModel.getMessageTemplateId();
        List<MessageParam> messageParamList = processModel.getMessageParamList();

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
    }
}
