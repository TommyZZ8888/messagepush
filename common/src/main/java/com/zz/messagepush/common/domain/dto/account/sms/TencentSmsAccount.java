package com.zz.messagepush.common.domain.dto.account.sms;

/**
 * @Description * 腾讯短信参数
 * *
 * * 参数示例：
 * * [{"sms_10":{"url":"sms.tencentcloudapi.com","region":"ap-guangzhou","secretId":"AKIDhDUUDfffffMEqBF1WljQq","secretKey":"B4h39yWnfffff7D2btue7JErDJ8gxyi","smsSdkAppId":"140025","templateId":"11897","signName":"Java3y公众号","supplierId":10,"supplierName":"腾讯云"}}]
 * *
 * @Author 张卫刚
 * @Date Created on 2023/3/28
 */

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TencentSmsAccount extends SmsAccount {

    /**
     * api相关
     */
    private String url;
    
    private String region;

    /**
     * 账号相关
     */
    private String secretId;

    private String secretKey;

    private String smsSdkAppId;

    private String templateId;

    private String signName;
}
