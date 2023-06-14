package com.zz.messagepush.handler.script.impl;


import com.zz.messagepush.common.domain.dto.account.WeChatOfficialAccount;
import com.zz.messagepush.handler.domain.wechat.WeChatOfficialParam;
import com.zz.messagepush.handler.script.OfficialAccountService;
import com.zz.messagepush.support.utils.WxServiceUtil;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.template.WxMpTemplateData;
import me.chanjar.weixin.mp.bean.template.WxMpTemplateMessage;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @Description
 * @Author 张卫刚
 * @Date Created on 2023/4/10
 */
@Service
public class OfficialAccountServiceImpl implements OfficialAccountService {

    @Override
    public List<String> send(WeChatOfficialParam weChatOfficialParam) throws Exception {
        WeChatOfficialAccount account = WxServiceUtil.wxOfficialAccountMap.get(weChatOfficialParam.getSendAccount());
        WxMpService wxMpService = WxServiceUtil.wxMpServiceMap.get(weChatOfficialParam.getSendAccount());
        List<WxMpTemplateMessage> messages = assemblyReq(weChatOfficialParam, account);
        List<String> messageIds = new ArrayList<>(messages.size());
        for (WxMpTemplateMessage wxMpTemplateMessage : messages) {
            String msgId = wxMpService.getTemplateMsgService().sendTemplateMsg(wxMpTemplateMessage);
            messageIds.add(msgId);
        }
        return messageIds;
    }


    private List<WxMpTemplateMessage> assemblyReq(WeChatOfficialParam param, WeChatOfficialAccount account) {
        Set<String> receivers = param.getOpenIds();
        List<WxMpTemplateMessage> wxMpTemplateMessages = new ArrayList<>(receivers.size());

        for (String receiver : receivers) {
            WxMpTemplateMessage wxMpTemplateMessage = WxMpTemplateMessage.builder()
                    .toUser(receiver)
                    .templateId(account.getTemplateId())
                    .url(account.getUrl())
                    .data(getWxMpTemplateData(param.getData()))
                    .miniProgram(new WxMpTemplateMessage.MiniProgram(account.getMiniProgramId(), account.getPath(), false))
                    .build();
            wxMpTemplateMessages.add(wxMpTemplateMessage);
        }
        return wxMpTemplateMessages;
    }

    private List<WxMpTemplateData> getWxMpTemplateData(Map<String, String> data) {
        List<WxMpTemplateData> wxMpTemplateDataList = new ArrayList<>(data.size());
        data.forEach((k, v) -> wxMpTemplateDataList.add(new WxMpTemplateData(k, v)));
        return wxMpTemplateDataList;
    }
}
