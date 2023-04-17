package com.zz.messagepush.handler.script;

import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.cp.bean.message.WxCpMessage;
import me.chanjar.weixin.cp.bean.message.WxCpMessageSendResult;

/**
 * @Description 企业微信发送消息
 * @Author 张卫刚
 * @Date Created on 2023/4/14
 */
public interface EnterpriseWeChatService {

    /**
     * 发送消息（目前只支持userid）
     * @param wxCpMessage
     * @return
     * @throws WxErrorException
     */
    WxCpMessageSendResult send(WxCpMessage wxCpMessage) throws WxErrorException;
}
