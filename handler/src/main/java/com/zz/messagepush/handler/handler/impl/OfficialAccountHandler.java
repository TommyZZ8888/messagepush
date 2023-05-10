package com.zz.messagepush.handler.handler.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.nacos.shaded.com.google.common.base.Throwables;
import com.zz.messagepush.common.domain.dto.model.OfficialAccountContentModel;
import com.zz.messagepush.common.domain.dto.TaskInfo;
import com.zz.messagepush.common.enums.ChannelType;
import com.zz.messagepush.handler.domain.wechat.WeChatOfficialParam;
import com.zz.messagepush.handler.handler.BaseHandler;
import com.zz.messagepush.handler.handler.Handler;
import com.zz.messagepush.handler.script.OfficialAccountService;
import com.zz.messagepush.support.domain.entity.MessageTemplateEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @Description 微信服务号推送处理
 * @Author 张卫刚
 * @Date Created on 2023/4/10
 */

@Component
@Slf4j
public class OfficialAccountHandler extends BaseHandler implements Handler {


    @Autowired
    private OfficialAccountService officialAccountScript;

    public OfficialAccountHandler() {
        channelCode = ChannelType.OFFICIAL_ACCOUNT.getCode();
    }


    @Override
    public boolean handler(TaskInfo taskInfo) {
        // 构建微信模板消息
        OfficialAccountContentModel contentModel = (OfficialAccountContentModel) taskInfo.getContentModel();
        WeChatOfficialParam officialParam = WeChatOfficialParam.builder()
                .openIds(taskInfo.getReceiver())
                .messageTemplateId(taskInfo.getMessageTemplateId())
                .sendAccount(taskInfo.getSendAccount())
                .data(contentModel.getMap())
                .build();

        try {
            List<String> send = officialAccountScript.send(officialParam);
            log.info("OfficialAccountHandler#handler successfully messageIds:{}", send);
            return true;
        } catch (Exception e) {
            log.error("OfficialAccountHandler#handler fail:{},params:{}",
                    Throwables.getStackTraceAsString(e), JSON.toJSONString(taskInfo));
        }
        return false;
    }

    @Override
    public void recall(MessageTemplateEntity messageTemplate) {

    }


    /**
     * 通过taskInfo构建模板消息
     *
     * @param taskInfo
     * @return
     */
   /** private List<WxMpTemplateMessage> buildTemplateMsg(TaskInfo taskInfo) {
        Set<String> receiver = taskInfo.getReceiver();
        Long messageTemplateId = taskInfo.getMessageTemplateId();
        OfficialAccountContentModel contentModel = (OfficialAccountContentModel) taskInfo.getContentModel();

        List<WxMpTemplateMessage> list = new ArrayList<>(receiver.size());
        String url = contentModel.getUrl();
        Map<String, String> map = contentModel.getMap();

        for (String userId : receiver) {
            WxMpTemplateMessage wxMpTemplateMessage = WxMpTemplateMessage.builder().toUser(userId)
                    .templateId(String.valueOf(messageTemplateId))
                    .url(url)
                    .build();

            map.forEach((k, v) -> wxMpTemplateMessage.addData(new WxMpTemplateData(k, v)));
            list.add(wxMpTemplateMessage);
        }
        return list;
    }*/
}
