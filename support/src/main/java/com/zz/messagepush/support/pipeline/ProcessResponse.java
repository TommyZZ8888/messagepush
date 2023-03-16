package com.zz.messagepush.support.pipeline;

import lombok.Builder;
import lombok.Data;

/**
 * @Description 流程处理的结果
 * @Author 张卫刚
 * @Date Created on 2023/3/16
 */

@Data
@Builder
public class ProcessResponse {

    /**
     * 返回值编码
     */
    private final String code;

    /**
     * 返回值描述
     */
    private final String description;
}
