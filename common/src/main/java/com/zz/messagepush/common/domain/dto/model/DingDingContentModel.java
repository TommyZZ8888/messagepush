package com.zz.messagepush.common.domain.dto.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Description 钉钉 自定义机器人
 * @Author 张卫刚
 * @Date Created on 2023/4/17
 */

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DingDingContentModel extends ContentModel {

    /**
     * 发送类型
     */
    private String sendType;

    /**
     * 文本消息 需要发送的内容
     */
    private String content;

    /**
     * 图片文本语音消息，需要发送使用的素材字段
     */
    private String mediaId;

}
