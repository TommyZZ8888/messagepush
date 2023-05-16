package com.zz.messagepush.common.domain.dto.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Description 飞书机器人
 * @Author 张卫刚
 * @Date Created on 2023/5/16
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FeiShuRobotContentModel extends ContentModel{


    private String sendType;

    private String content;

    private String title;

    private String mediaId;

    /**
     * 富文本内容：[[{"tag":"text","text":"项目有更新: "},{"tag":"a","text":"请查看","href":"http://www.example.com/"},{"tag":"at","user_id":"ou_18eac8********17ad4f02e8bbbb"}]]
     */
    private String postContent;

}
