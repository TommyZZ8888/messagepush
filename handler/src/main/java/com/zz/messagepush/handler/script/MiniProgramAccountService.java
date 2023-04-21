package com.zz.messagepush.handler.script;

import com.zz.messagepush.handler.domain.wechat.WeChatMiniProgramParam;

/**
 * @Description MiniProgramAccountService
 * @Author 张卫刚
 * @Date Created on 2023/4/21
 */
public interface MiniProgramAccountService {

    /**
     * 发送订阅消息
     *
     * @param weChatMiniProgramParam
     * @throws Exception
     */
    void send(WeChatMiniProgramParam weChatMiniProgramParam) throws Exception;

}
