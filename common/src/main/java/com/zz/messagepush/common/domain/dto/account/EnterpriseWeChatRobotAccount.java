package com.zz.messagepush.common.domain.dto.account;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Description 企业微信 机器人 账号信息
 * @Author 张卫刚
 * @Date Created on 2023/5/12
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EnterpriseWeChatRobotAccount {

    /**
     * 自定义机器人中的webhook
     */
    private String webhook;
}
