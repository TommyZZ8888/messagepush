package com.zz.messagepush.handler.script;

import com.zz.messagepush.handler.domain.wechat.WeChatOfficialParam;
import me.chanjar.weixin.common.error.WxErrorException;

import java.util.List;

/**
 * @Description
 * @Author 张卫刚
 * @Date Created on 2023/4/10
 */
public interface OfficialAccountService {


    /**
     * 发送模板消息
     * @param weChatOfficialParam 模板消息参数
     * @return
     */
    List<String> send(WeChatOfficialParam weChatOfficialParam) throws Exception;
}
