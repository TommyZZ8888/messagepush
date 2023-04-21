package com.zz.messagepush.handler.script.impl;

import com.zz.messagepush.handler.script.EnterpriseWeChatService;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.cp.api.WxCpService;
import me.chanjar.weixin.cp.api.impl.WxCpMessageServiceImpl;
import me.chanjar.weixin.cp.api.impl.WxCpServiceImpl;
import me.chanjar.weixin.cp.bean.message.WxCpMessage;
import me.chanjar.weixin.cp.bean.message.WxCpMessageSendResult;
import me.chanjar.weixin.cp.config.WxCpConfigStorage;
import me.chanjar.weixin.cp.config.impl.WxCpDefaultConfigImpl;
import org.springframework.stereotype.Service;

/**
 * @Description
 * @Author 张卫刚
 * @Date Created on 2023/4/14
 */

@Slf4j
@Service
public class EnterpriseWeChatServiceImpl implements EnterpriseWeChatService {


    @Override
    public WxCpMessageSendResult send(WxCpMessage wxCpMessage) throws WxErrorException {
        WxCpMessageServiceImpl wxCpMessageService = new WxCpMessageServiceImpl(initService());

        return wxCpMessageService.send(wxCpMessage);
    }


    public WxCpService initService() {
        WxCpService wxCpService = new WxCpServiceImpl();
        wxCpService.setWxCpConfigStorage(initConfig());
        return wxCpService;
    }


    private WxCpConfigStorage initConfig() {
        WxCpDefaultConfigImpl wxCpConfigStorage = new WxCpDefaultConfigImpl();
        wxCpConfigStorage.setAesKey("");
        wxCpConfigStorage.setCorpId("");
        wxCpConfigStorage.setAgentId(1);
        wxCpConfigStorage.setAesKey("");
        wxCpConfigStorage.setToken("");
        return wxCpConfigStorage;
    }
}
