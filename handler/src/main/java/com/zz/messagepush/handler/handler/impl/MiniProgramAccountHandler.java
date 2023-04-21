package com.zz.messagepush.handler.handler.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.nacos.shaded.com.google.common.base.Throwables;
import com.zz.messagepush.common.domain.dto.TaskInfo;
import com.zz.messagepush.common.domain.dto.model.MiniProgramContentModel;
import com.zz.messagepush.common.enums.ChannelType;
import com.zz.messagepush.handler.domain.wechat.WeChatMiniProgramParam;
import com.zz.messagepush.handler.handler.BaseHandler;
import com.zz.messagepush.handler.handler.Handler;
import com.zz.messagepush.handler.script.MiniProgramAccountService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @Description MiniProgramHandler
 * @Author 张卫刚
 * @Date Created on 2023/4/21
 */
@Component
@Slf4j
public class MiniProgramAccountHandler extends BaseHandler implements Handler {

    @Autowired
    private MiniProgramAccountService miniProgramAccountService;

    public MiniProgramAccountHandler() {
        channelCode = ChannelType.MINI_PROGRAM.getCode();
    }


    @Override
    public boolean handler(TaskInfo taskInfo) {
        WeChatMiniProgramParam weChatMiniProgramParam = buildMiniProgramParam(taskInfo);

        try {
            miniProgramAccountService.send(weChatMiniProgramParam);
        } catch (Exception e) {
            log.error("miniProgramAccountHandler#handler fail:{},params:{}", Throwables.getStackTraceAsString(e), JSON.toJSONString(taskInfo));
            return false;
        }
        return true;
    }


    private WeChatMiniProgramParam buildMiniProgramParam(TaskInfo taskInfo) {
       //小程序订阅消息可以关联到系统业务，通过接口查询
        WeChatMiniProgramParam miniProgramParam = WeChatMiniProgramParam.builder()
                .messageTemplateId(taskInfo.getMessageTemplateId())
                .sendAccount(taskInfo.getSendAccount())
                .openIds(taskInfo.getReceiver()).build();
        MiniProgramContentModel contentModel = (MiniProgramContentModel) taskInfo.getContentModel();

        miniProgramParam.setData(contentModel.getParam());
        return miniProgramParam;
    }
}
