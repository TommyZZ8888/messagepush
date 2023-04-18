package com.zz.messagepush.support.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.ctrip.framework.apollo.Config;
import com.ctrip.framework.apollo.spring.annotation.ApolloConfig;
import com.zz.messagepush.common.constant.AustinConstant;
import org.springframework.stereotype.Component;

/**
 * @Description
 * @Author 张卫刚
 * @Date Created on 2023/3/28
 */

@Component
public class AccountUtils {

    @ApolloConfig("boss.austin")
    private Config config;

    /**
     * 短信参数示例：[{"sms_10":{"url":"sms.tencentcloudapi.com","region":"ap-guangzhou","secretId":"AKIDhDUUDfffffMEqBF1WljQq","secretKey":"B4h39yWnfffff7D2btue7JErDJ8gxyi","smsSdkAppId":"140025","templateId":"11897","signName":"Java3y公众号","supplierId":10,"supplierName":"腾讯云"}}]
     * 邮件参数示例：[{"email_10":{"host":"smtp.qq.com","port":465,"user":"403686131@qq.com","pass":"","from":"403686131@qq.com"}}]
     */
    public <T> T getAccount(Integer sendAccount, String apolloKey, String prefix, T t) {
        String accountValues = config.getProperty(apolloKey, AustinConstant.APOLLO_DEFAULT_VALUE_JSON_ARRAY);
        JSONArray jsonArray = JSON.parseArray(accountValues);
        for (int i = 0; i < jsonArray.size(); i++) {
            JSONObject jsonObject = jsonArray.getJSONObject(i);
            Object object = jsonObject.getObject(prefix + sendAccount, t.getClass());
            if (object != null) {
                return (T) object;
            }
        }
        return null;
    }

}
