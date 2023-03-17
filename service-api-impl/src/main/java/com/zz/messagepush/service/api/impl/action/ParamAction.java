package com.zz.messagepush.service.api.impl.action;

import cn.hutool.core.collection.CollUtil;
import com.zz.messagepush.common.domain.ResponseResult;
import com.zz.messagepush.common.enums.RespStatusEnum;
import com.zz.messagepush.service.api.domain.dto.MessageParam;
import com.zz.messagepush.service.api.impl.domain.SendTaskModel;
import com.zz.messagepush.support.pipeline.BusinessProcess;
import com.zz.messagepush.support.pipeline.ProcessContext;

import java.util.List;

/**
 * @Description 参数校验
 * @Author 张卫刚
 * @Date Created on 2023/3/16
 */
public class ParamAction implements BusinessProcess {
    @Override
    public void process(ProcessContext context) {
        SendTaskModel processModel = (SendTaskModel) context.getProcessModel();

        Long messageTemplateId = processModel.getMessageTemplateId();
        List<MessageParam> messageParamList = processModel.getMessageParamList();

        if (messageTemplateId == null || CollUtil.isEmpty(messageParamList)) {
            context.setNeedBreak(true);
            context.setResponse(ResponseResult.fail(RespStatusEnum.CLIENT_BAD_PARAMETERS.getDescription()));
        }
    }
}
