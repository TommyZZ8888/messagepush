package com.zz.messagepush.common.enums;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
/**
 * @Description 简单的埋点信息
 * @Author 张卫刚
 * @Date Created on 2023/4/10
 */


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SimpleAnchorInfo {

    /**
     * 具体点位
     */
    private int state;

    /**
     * 业务id（数据追踪使用）
     * 生成逻辑参考TaskInfoUtil
     */
    private Long businessId;

    /**
     * 生成时间
     */
    private Long timestamp;
}
