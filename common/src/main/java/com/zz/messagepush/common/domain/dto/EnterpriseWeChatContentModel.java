package com.zz.messagepush.common.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Description 企业微信
 * @Author 张卫刚
 * @Date Created on 2023/4/14
 */

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EnterpriseWeChatContentModel extends ContentModel {

    /**
     * 下发企业微信的消息类型
     */
    private String messageType;

    /**
     * 文案
     */
    private String content;

    /**
     * 媒体类型
     */
    private String mediaType;


    /**
     *  其他消息类型： https://developer.work.weixin.qq.com/document/path/90372#%E6%96%87%E6%9C%AC%E6%B6%88%E6%81%AF
     */
}
