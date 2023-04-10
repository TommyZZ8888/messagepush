package com.zz.messagepush.handler.script;


import cn.binarywang.wx.miniapp.api.WxMaService;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.api.impl.WxMpServiceImpl;
import me.chanjar.weixin.mp.bean.template.WxMpTemplateMessage;
import me.chanjar.weixin.mp.config.impl.WxMpDefaultConfigImpl;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @Description
 * @Author 张卫刚
 * @Date Created on 2023/4/10
 */
@Service
public class WxMpTemplateScript implements OfficialAccountScript {


    @Value("${wx.mp.account.appid}")
    private String appId;
    @Value("${wx.mp.account.secret}")
    private String secret;
    @Value("${wx.mp.account.token}")
    private String token;
    @Value("${wx.mp.account.aesKey}")
    private String aesKey;


    @Override
    public List<String> send(List<WxMpTemplateMessage> wxMpTemplateMessages) throws WxErrorException {
        WxMpService wxMpService = initAccount();
        List<String> list = new ArrayList<>(wxMpTemplateMessages.size());

        for (WxMpTemplateMessage wxMpTemplateMessage : wxMpTemplateMessages) {
            String id = wxMpService.getTemplateMsgService().sendTemplateMsg(wxMpTemplateMessage);
            list.add(id);
        }
        return list;
    }


    /**
     * 初始化账号
     * @return
     */
    public WxMpService initAccount() {
        WxMpService wxMpService = new WxMpServiceImpl();
        WxMpDefaultConfigImpl wxMpDefaultConfig = new WxMpDefaultConfigImpl();
        wxMpDefaultConfig.setAesKey(aesKey);
        wxMpDefaultConfig.setToken(token);
        wxMpDefaultConfig.setSecret(secret);
        wxMpDefaultConfig.setAppId(appId);
        wxMpService.setWxMpConfigStorage(wxMpDefaultConfig);
        return wxMpService;
    }


}
