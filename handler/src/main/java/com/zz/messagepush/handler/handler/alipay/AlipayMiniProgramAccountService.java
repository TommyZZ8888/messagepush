package com.zz.messagepush.handler.handler.alipay;

import com.alipay.api.AlipayApiException;
import com.zz.messagepush.handler.domain.alipay.AlipayMiniProgramParam;

/**
 * @Description 支付宝小程序发送订阅消息接口
 * @Author 张卫刚
 * @Date Created on 2023/6/12
 */
public interface AlipayMiniProgramAccountService {

    /**
     * 发送订阅消息
     * @param alipayMiniProgramParam
     * @throws AlipayApiException
     */
    void send(AlipayMiniProgramParam alipayMiniProgramParam) throws AlipayApiException;
}
