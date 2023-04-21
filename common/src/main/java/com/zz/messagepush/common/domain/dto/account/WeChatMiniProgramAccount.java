package com.zz.messagepush.common.domain.dto.account;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Description WeChatMiniProgramAccount
 * @Author 张卫刚
 * @Date Created on 2023/4/21
 */

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WeChatMiniProgramAccount {

    /**
     * 账号相关
     */
    private String appId;

    private String appSecret;

    private String grantType;

    /**
     * 消息模板id
     */
    private String templateId;

    /**
     * 点击跳转的界面
     */
    private String page;

    /**
     * 跳转小程序类型
     */
    private String miniProgramState;
}
