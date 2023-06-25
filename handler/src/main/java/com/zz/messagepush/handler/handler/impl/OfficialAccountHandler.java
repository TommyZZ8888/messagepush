package com.zz.messagepush.handler.handler.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.nacos.shaded.com.google.common.base.Throwables;
import com.zz.messagepush.common.domain.dto.model.OfficialAccountContentModel;
import com.zz.messagepush.common.domain.dto.TaskInfo;
import com.zz.messagepush.common.enums.ChannelType;
import com.zz.messagepush.handler.handler.BaseHandler;
import com.zz.messagepush.handler.handler.Handler;
import com.zz.messagepush.support.domain.entity.MessageTemplateEntity;
import com.zz.messagepush.support.utils.AccountUtils;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.template.WxMpTemplateData;
import me.chanjar.weixin.mp.bean.template.WxMpTemplateMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @Description 微信服务号推送处理
 * @Author 张卫刚
 * @Date Created on 2023/4/10
 */

@Component
@Slf4j
public class OfficialAccountHandler extends BaseHandler implements Handler {


    @Autowired
    private AccountUtils accountUtils;

    public OfficialAccountHandler() {
        channelCode = ChannelType.OFFICIAL_ACCOUNT.getCode();
    }


    @Override
    public boolean handler(TaskInfo taskInfo) {
        try {
            WxMpService wxMpService = accountUtils.getAccountById(taskInfo.getSendAccount(), WxMpService.class);
            List<WxMpTemplateMessage> wxMpTemplateMessages = assembleReq(taskInfo.getReceiver(), (OfficialAccountContentModel) taskInfo.getContentModel());
            for (WxMpTemplateMessage message : wxMpTemplateMessages) {
                wxMpService.getTemplateMsgService().sendTemplateMsg(message);
            }
            log.info("OfficialAccountHandler#handler successfully messageIds:{}", wxMpTemplateMessages);
            return true;
        } catch (Exception e) {
            log.error("OfficialAccountHandler#handler fail:{},params:{}",
                    Throwables.getStackTraceAsString(e), JSON.toJSONString(taskInfo));
        }
        return false;
    }


    /**
     * 组装发送模板信息参数
     */
    private List<WxMpTemplateMessage> assembleReq(Set<String> receiver, OfficialAccountContentModel contentModel) {
        List<WxMpTemplateMessage> wxMpTemplateMessages = new ArrayList<>(receiver.size());
        for (String openId : receiver) {
            WxMpTemplateMessage templateMessage = WxMpTemplateMessage.builder()
                    .toUser(openId)
                    .templateId(contentModel.getTemplateId())
                    .url(contentModel.getUrl())
                    .data(getWxMpTemplateData(contentModel.getOfficialAccountParam()))
                    .miniProgram(new WxMpTemplateMessage.MiniProgram(contentModel.getMiniProgramId(), contentModel.getUrl(), false))
                    .build();
            wxMpTemplateMessages.add(templateMessage);
        }
        return wxMpTemplateMessages;
    }

    /**
     * 构建模板消息参数
     * @return
     */
    private List<WxMpTemplateData> getWxMpTemplateData(Map<String, String> data) {
        List<WxMpTemplateData> templateDataList = new ArrayList<>(data.size());
        data.forEach((k, v) -> templateDataList.add(new WxMpTemplateData(k, v)));
        return templateDataList;
    }

    @Override
    public void recall(MessageTemplateEntity messageTemplate) {

    }
}
