package com.zz.messagepush.common.domain.dto.account;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Description 模板消息参数
 * 参数示例：
 * https://developers.weixin.qq.com/doc/offiaccount/Message_Management/Template_Message_Interface.html
 * @Author 张卫刚
 * @Date Created on 2023/4/10
 */

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WeChatOfficialAccount {

    /**
     * 用户关注的服务号id
     */
    private String secret;

    /**
     * 消息模板id
     */
    private String templateId;

    /**
     * 消息跳转的url
     */
    private String url;

    /**
     * 模板消息跳转的小程序id
     */
    private String miniProgramId;

    /**
     * 消息跳转的页面路径
     */
    private String path;

    /**
     * 小程序appid
     */
    private String appId;

    private String token;

}
