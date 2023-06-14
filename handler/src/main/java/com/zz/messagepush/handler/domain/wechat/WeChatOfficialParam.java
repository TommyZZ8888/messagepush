package com.zz.messagepush.handler.domain.wechat;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;
import java.util.Set;

/**
 * @Description 服务号参数
 * @Author 张卫刚
 * @Date Created on 2023/4/20
 */


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WeChatOfficialParam {

    /**
     * 业务id
     */
    private Long messageTemplateId;

    /**
     * 关注服务号的用户
     */
    private Set<String> openIds;

    /**
     * 模板消息的载体
     */
    private Map<String, String> data;

    /**
     * 发送账户
     */
    private Long sendAccount;
}
