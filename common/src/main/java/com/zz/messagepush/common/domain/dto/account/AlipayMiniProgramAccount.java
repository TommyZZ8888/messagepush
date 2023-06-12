package com.zz.messagepush.common.domain.dto.account;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Description 支付宝小程序订阅消息账号配置
 * @Author 张卫刚
 * @Date Created on 2023/6/12
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AlipayMiniProgramAccount {

    private String privateKey;

    private String alipayPublicKey;

    private String appId;

    private String userTemplateId;

    private String page;

}
