package com.zz.messagepush.handler.config;

import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayConfig;
import com.alipay.api.DefaultAlipayClient;
import com.zz.messagepush.common.domain.dto.account.AlipayMiniProgramAccount;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * @Description AlipayClientSingleton
 * @Author 张卫刚
 * @Date Created on 2023/6/14
 */
public class AlipayClientSingleton {

    private volatile static DefaultAlipayClient defaultAlipayClient;

    private static Map<String, DefaultAlipayClient> defaultAlipayClientMap = new HashMap<>();

    private AlipayClientSingleton() {
    }

    public static DefaultAlipayClient getSingleton(AlipayMiniProgramAccount account) throws AlipayApiException {
//        DefaultAlipayClient alipayClient = defaultAlipayClientMap.get(account.getAppId());
//        if (Objects.isNull(alipayClient)) {
        //优化
        if (defaultAlipayClientMap.containsKey(account.getAppId())) {
            synchronized (DefaultAlipayClient.class) {
                if (defaultAlipayClientMap.containsKey(account.getAppId())) {
                    AlipayConfig alipayConfig = new AlipayConfig();
                    alipayConfig.setServerUrl("https://openapi.alipaydev.com/gateway.do");
                    alipayConfig.setAppId(account.getAppId());
                    alipayConfig.setPrivateKey(account.getPrivateKey());
                    alipayConfig.setFormat("json");
                    alipayConfig.setAlipayPublicKey(account.getAlipayPublicKey());
                    alipayConfig.setCharset("utf-8");
                    alipayConfig.setSignType("RSA2");
                    defaultAlipayClient = new DefaultAlipayClient(alipayConfig);
                    defaultAlipayClientMap.put(account.getAppId(), defaultAlipayClient);
                }
            }
        }
        return defaultAlipayClientMap.get(account.getAppId());
    }
}
