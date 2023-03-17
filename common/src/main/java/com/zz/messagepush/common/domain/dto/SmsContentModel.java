package com.zz.messagepush.common.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Description 短信内容模型
 * @Author 张卫刚
 * @Date Created on 2023/3/16
 */

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SmsContentModel extends ContentModel {

    /**
     * 短信发送内容
     */
    private String content;

    /**
     * 短信发送链接
     */
    private String url;

}
