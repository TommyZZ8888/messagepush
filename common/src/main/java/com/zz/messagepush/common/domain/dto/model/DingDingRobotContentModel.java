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
public class DingDingRobotContentModel extends ContentModel {

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

    /**
     * 钉钉机器人 markdown消息类型标题，feedcard消息类型标题，actioncard消息标题
     */
    private String title;

    /**
     * actioncard消息类型 按钮布局
     */
    private String btnOrientation;

    /**
     * 链接信息点击跳转的url
     */
    private String url;

    /**
     * 钉钉机器人：【ActionCard消息】按钮的文案和跳转链接的json
     * [{\"title\":\"别点我\",\"actionURL\":\"https://www.baidu.com/\"},{\"title\":\"没关系，还是点我把\",\"actionURL\":\"https://www.baidu.com/\\t\"}]
     */
    private String btns;

    /**
     * 链接信息图片url
     */
    private String picUrl;

    /**
     * feedCard消息体
     */
    private String feedCards;

}
