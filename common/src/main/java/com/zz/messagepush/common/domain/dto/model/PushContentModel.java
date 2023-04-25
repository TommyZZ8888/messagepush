package com.zz.messagepush.common.domain.dto.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Description
 * @Author 张卫刚
 * @Date Created on 2023/3/16
 */

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PushContentModel extends ContentModel {

    private String title;

    private String content;

    private String url;
}
