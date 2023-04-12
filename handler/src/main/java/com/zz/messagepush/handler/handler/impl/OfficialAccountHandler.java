package com.zz.messagepush.handler.handler.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.nacos.shaded.com.google.common.base.Throwables;
import com.zz.messagepush.common.domain.dto.OfficialAccountContentModel;
import com.zz.messagepush.common.domain.dto.TaskInfo;
import com.zz.messagepush.common.enums.ChannelType;
import com.zz.messagepush.handler.handler.BaseHandler;
import com.zz.messagepush.handler.handler.Handler;
import com.zz.messagepush.handler.script.OfficialAccountScript;
import lombok.extern.slf4j.Slf4j;
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
    private OfficialAccountScript officialAccountScript;

    public OfficialAccountHandler() {
        channelCode = ChannelType.OFFICIAL_ACCOUNT.getCode();
    }


    @Override
    public boolean handler(TaskInfo taskInfo) {
        List<WxMpTemplateMessage> wxMpTemplateMessages = buildTemplateMsg(taskInfo);

        try {
            officialAccountScript.send(wxMpTemplateMessages);
            log.info("OfficialAccountHandler#handler successfully messageIds:{}", wxMpTemplateMessages);
            return true;
        } catch (Exception e) {
            log.error("OfficialAccountHandler#handler fail:{},params:{}",
                    Throwables.getStackTraceAsString(e), JSON.toJSONString(taskInfo));
        }
        return false;
    }


    /**
     * 通过taskInfo构建模板消息
     *
     * @param taskInfo
     * @return
     */
    private List<WxMpTemplateMessage> buildTemplateMsg(TaskInfo taskInfo) {
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
    }
}
