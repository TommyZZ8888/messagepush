package com.zz.messagepush.handler.domain.sms;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Description 对于每种消息类型的短信配置
 * @Author 张卫刚
 * @Date Created on 2023/5/4
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MessageTypeSmsConfig {

    /**
     * 权重（  决定流量的占比）
     */
    private Integer weights;

    /**
     * 脚本名称
     */
    private String scriptName;

}
