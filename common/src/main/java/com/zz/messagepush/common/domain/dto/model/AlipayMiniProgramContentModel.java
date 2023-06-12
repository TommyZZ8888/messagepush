package com.zz.messagepush.common.domain.dto.model;

import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Description 支付宝小程序订阅消息内容
 * @Author 张卫刚
 * @Date Created on 2023/6/12
 */

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AlipayMiniProgramContentModel extends ContentModel {

    /**
     * 接收消息发送的数据
     */
    private Map<String, String> map;
}
