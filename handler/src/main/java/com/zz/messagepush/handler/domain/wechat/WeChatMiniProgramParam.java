package com.zz.messagepush.handler.domain.wechat;

import lombok.Builder;
import lombok.Data;

import java.security.SecureRandom;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @Description WeChatMiniProgramParam
 * @Author 张卫刚
 * @Date Created on 2023/4/21
 */

@Data
@Builder
public class WeChatMiniProgramParam {

    /**
     * 业务id
     */
    private Long messageTemplateId;

    /**
     * 发送账号
     */
    private Integer sendAccount;

    /**
     * 接收者的openid
     */
    private Set<String> openIds;

    /**
     * 模板内容，格式形如 { "key1": { "value": any }, "key2": { "value": any } }
     */
    private Map<String, String> data;

}
