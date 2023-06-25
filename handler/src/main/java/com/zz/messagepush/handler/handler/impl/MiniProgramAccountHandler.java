package com.zz.messagepush.handler.handler.impl;

import cn.binarywang.wx.miniapp.api.WxMaService;
import cn.binarywang.wx.miniapp.api.WxMaSubscribeService;
import cn.binarywang.wx.miniapp.bean.WxMaSubscribeMessage;
import com.alibaba.fastjson.JSON;
import com.alibaba.nacos.shaded.com.google.common.base.Throwables;
import com.zz.messagepush.common.domain.dto.TaskInfo;
import com.zz.messagepush.common.domain.dto.model.MiniProgramContentModel;
import com.zz.messagepush.common.enums.ChannelType;
import com.zz.messagepush.handler.handler.BaseHandler;
import com.zz.messagepush.handler.handler.Handler;
import com.zz.messagepush.support.domain.entity.MessageTemplateEntity;
import com.zz.messagepush.support.utils.AccountUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @Description MiniProgramHandler
 * @Author 张卫刚
 * @Date Created on 2023/4/21
 */
@Component
@Slf4j
public class MiniProgramAccountHandler extends BaseHandler implements Handler {
    
    @Autowired
    private AccountUtils accountUtils;

    public MiniProgramAccountHandler() {
        channelCode = ChannelType.MINI_PROGRAM.getCode();
    }


    @Override
    public boolean handler(TaskInfo taskInfo) {
        MiniProgramContentModel contentModel = (MiniProgramContentModel) taskInfo.getContentModel();
        WxMaService wxMaService = accountUtils.getAccountById(taskInfo.getSendAccount(), WxMaService.class);
        List<WxMaSubscribeMessage> wxMaSubscribeMessages = assembleReq(taskInfo.getReceiver(), contentModel);
        for (WxMaSubscribeMessage message : wxMaSubscribeMessages) {
            try {
                wxMaService.getSubscribeService().sendSubscribeMsg(message);
            } catch (Exception e) {
                log.info("MiniProgramAccountHandler#handler fail! param:{},e:{}", JSON.toJSONString(taskInfo), Throwables.getStackTraceAsString(e));
            }
        }
        return true;
    }


    private List<WxMaSubscribeMessage> assembleReq(Set<String> receiver, MiniProgramContentModel contentModel) {
        List<WxMaSubscribeMessage> messageList = new ArrayList<>(receiver.size());
        for (String openId : receiver) {
            WxMaSubscribeMessage subscribeMessage = WxMaSubscribeMessage.builder()
                    .toUser(openId)
                    .data(getWxMaTemplateData(contentModel.getMiniProgramParam()))
                    .templateId(contentModel.getTemplateId())
                    .page(contentModel.getPage())
                    .build();
            messageList.add(subscribeMessage);
        }
        return messageList;
    }

    private List<WxMaSubscribeMessage.MsgData> getWxMaTemplateData(Map<String, String> data) {
        List<WxMaSubscribeMessage.MsgData> templateDataList = new ArrayList<>(data.size());
        data.forEach((k, v) -> templateDataList.add(new WxMaSubscribeMessage.MsgData(k, v)));
        return templateDataList;
    }


    @Override
    public void recall(MessageTemplateEntity messageTemplate) {

    }
}
