package com.zz.messagepush.common.domain.dto.account;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Description
 * @Author 张卫刚
 * @Date Created on 2023/4/17
 */

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DingDingRobotAccount {
    /**
     * 密钥
     */
    private String secret;

    /**
     * 自定义群机器人中的webhook
     */
    private String webhook;
}
