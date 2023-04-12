package com.zz.messagepush.handler.script;

import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.mp.bean.template.WxMpTemplateMessage;

import java.util.List;

/**
 * @Description
 * @Author 张卫刚
 * @Date Created on 2023/4/10
 */
public interface OfficialAccountScript {


    /**
     * 发送模板消息
     * @param wxMpTemplateMessages
     * @return
     */
    List<String> send(List<WxMpTemplateMessage> wxMpTemplateMessages) throws WxErrorException;
}
