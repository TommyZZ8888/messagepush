package com.zz.messagepush.common.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Description
 * @Author 张卫刚
 * @Date Created on 2023/3/16
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmailContentModel extends ContentModel {

    /**
     * 标题
     */
    public String title;

    /***
     * 内容（可写入html）
     */
    public String content;
}
