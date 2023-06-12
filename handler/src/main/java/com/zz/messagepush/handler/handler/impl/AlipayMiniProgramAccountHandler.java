package com.zz.messagepush.handler.handler.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.nacos.shaded.com.google.common.base.Throwables;
import com.zz.messagepush.common.domain.dto.TaskInfo;
import com.zz.messagepush.common.domain.dto.model.AlipayMiniProgramContentModel;
import com.zz.messagepush.common.enums.ChannelType;
import com.zz.messagepush.handler.domain.alipay.AlipayMiniProgramParam;
import com.zz.messagepush.handler.handler.BaseHandler;
import com.zz.messagepush.handler.handler.Handler;
import com.zz.messagepush.handler.handler.alipay.AlipayMiniProgramAccountService;
import com.zz.messagepush.support.domain.entity.MessageTemplateEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @Description AlipayMiniProgramAccountHandler
 * @Author 张卫刚
 * @Date Created on 2023/6/12
 */
@Component
@Slf4j
public class AlipayMiniProgramAccountHandler extends BaseHandler implements Handler {

    @Autowired
    private AlipayMiniProgramAccountService alipayMiniProgramAccountService;

    public AlipayMiniProgramAccountHandler() {
        channelCode = ChannelType.ALIPAY_MINI_PROGRAM.getCode();
    }


    @Override
    public boolean handler(TaskInfo taskInfo) {
        AlipayMiniProgramParam param = buildMiniProgramParam(taskInfo);

        try {
            alipayMiniProgramAccountService.send(param);
        } catch (Exception e) {
            log.error("AlipayMiniProgramAccountHandler#handler fail:{},params:{}",
                    Throwables.getStackTraceAsString(e), JSON.toJSONString(taskInfo));
            return false;
        }
        return true;
    }

    @Override
    public void recall(MessageTemplateEntity messageTemplate) {

    }

    private AlipayMiniProgramParam buildMiniProgramParam(TaskInfo taskInfo) {
        AlipayMiniProgramParam param = AlipayMiniProgramParam.builder()
                .sendAccount(taskInfo.getSendAccount())
                .toUserId(taskInfo.getReceiver())
                .messageTemplateId(taskInfo.getMessageTemplateId()).build();
        AlipayMiniProgramContentModel contentModel = (AlipayMiniProgramContentModel) taskInfo.getContentModel();
        param.setData(contentModel.getMap());
        return param;
    }

}
