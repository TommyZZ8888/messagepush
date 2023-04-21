package com.zz.messagepush.handler.script.impl;

import cn.binarywang.wx.miniapp.api.WxMaService;
import cn.binarywang.wx.miniapp.api.impl.WxMaServiceImpl;
import cn.binarywang.wx.miniapp.api.impl.WxMaSubscribeServiceImpl;
import cn.binarywang.wx.miniapp.bean.WxMaSubscribeMessage;
import cn.binarywang.wx.miniapp.config.impl.WxMaDefaultConfigImpl;
import com.zz.messagepush.common.constant.SendAccountConstant;
import com.zz.messagepush.common.domain.dto.account.WeChatMiniProgramAccount;
import com.zz.messagepush.handler.domain.wechat.WeChatMiniProgramParam;
import com.zz.messagepush.handler.script.MiniProgramAccountService;
import com.zz.messagepush.support.utils.AccountUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @Description MiniProgramAccountServiceImpl
 * @Author 张卫刚
 * @Date Created on 2023/4/21
 */

@Service
public class MiniProgramAccountServiceImpl implements MiniProgramAccountService {

    @Autowired
    private AccountUtils accountUtils;

    @Override
    public void send(WeChatMiniProgramParam weChatMiniProgramParam) throws Exception {
        WeChatMiniProgramAccount account = accountUtils.getAccount(weChatMiniProgramParam.getSendAccount(), SendAccountConstant.WECHAT_MINI_PROGRAM_ACCOUNT_KEY, SendAccountConstant.WECHAT_MINI_PROGRAM_PREFIX, WeChatMiniProgramAccount.class);
        List<WxMaSubscribeMessage> messageList = assembleReq(weChatMiniProgramParam, account);

        WxMaSubscribeServiceImpl wxMaSubscribeService = initService(account);
        for (WxMaSubscribeMessage wxMaSubscribeMessage : messageList) {
            wxMaSubscribeService.sendSubscribeMsg(wxMaSubscribeMessage);
        }
    }


    /**
     * 组装发送模板信息参数
     *
     * @param param
     * @param account
     * @return
     */
    private List<WxMaSubscribeMessage> assembleReq(WeChatMiniProgramParam param, WeChatMiniProgramAccount account) {
        List<String> receiver = new ArrayList<>(param.getOpenIds());
        List<WxMaSubscribeMessage> messageList = new ArrayList<>(receiver.size());

        for (String openId : receiver) {
            WxMaSubscribeMessage message = WxMaSubscribeMessage.builder()
                    .toUser(openId)
                    .templateId(account.getTemplateId())
                    .miniprogramState(account.getMiniProgramState())
                    .page(account.getPage())
                    .data(getWxTemplateData(param.getData()))
                    .build();
            messageList.add(message);
        }
        return messageList;
    }

    /**
     * 构建订阅消息参数
     *
     * @param map
     * @return
     */
    public List<WxMaSubscribeMessage.MsgData> getWxTemplateData(Map<String, String> map) {
        List<WxMaSubscribeMessage.MsgData> data = new ArrayList<>(map.size());
        map.forEach((k, v) -> data.add(new WxMaSubscribeMessage.MsgData(k, v)));
        return data;
    }


    /**
     * 初始化微信小程序
     *
     * @param account
     * @return
     */
    private WxMaSubscribeServiceImpl initService(WeChatMiniProgramAccount account) {
        WxMaServiceImpl wxMaService = new WxMaServiceImpl();
        WxMaDefaultConfigImpl wxMaDefaultConfig = new WxMaDefaultConfigImpl();
        wxMaDefaultConfig.setAppid(account.getAppId());
        wxMaDefaultConfig.setSecret(account.getAppSecret());
        wxMaService.setWxMaConfig(wxMaDefaultConfig);
        return new WxMaSubscribeServiceImpl(wxMaService);
    }
}
