package com.zz.messagepush.common.domain.dto.model;

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
    private String sendType;

    /**
     * 文案
     */
    private String content;

    /**
     * 媒体类型
     */
    private String mediaId;


    /**
     * 标题
     */
    private String title;

    /**
     * 描述
     */
    private String description;

    /**
     * 链接
     */
    private String url;

    /**
     * 按钮文案
     */
    private String btnTxt;

    /**
     * 图文消息
     */
    private String articles;

    private String mpNewsArticle;

    /**
     * 小程序
     */
    private String appId;

    private String page;

    private Boolean emphasisFirstItem;

    private String contentItems;
}
