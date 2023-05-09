package com.zz.messagepush.common.domain.dto.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Description DingDingWorkContentModel
 * @Author 张卫刚
 * @Date Created on 2023/5/6
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DingDingWorkContentModel extends ContentModel {

    private String sendType;

    private String content;

    private String title;

    private String btnOrientation;

    private String btns;

    private String url;

    private String mediaId;

    private String duration;

    private String dingDingOaHead;

    private String dingDingOaBody;
}
