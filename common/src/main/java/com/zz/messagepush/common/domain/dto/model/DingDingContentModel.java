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
public class DingDingContentModel extends ContentModel{

    private String content;
}
