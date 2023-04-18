package com.zz.messagepush.common.domain.dto.account;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Description 钉钉工作消息 账号消息
 * appkey appsecret agentid都可在钉钉开发者后台的应用详情页获取
 * @Author 张卫刚
 * @Date Created on 2023/4/18
 */

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DingDingWorkNoticeAccount {

    /**
     * 应用的唯一标识key
     */
    public String appKey;

    /**
     * 应用的密钥
     */
    public String appSecret;

    /**
     * 发送消息时使用的微应用的AgentID
     */
    public String agentId;
}
