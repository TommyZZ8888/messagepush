package com.zz.messagepush.handler.domain.alipay;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;
import java.util.Set;

/**
 * @Description 支付宝小程序参数
 * @Author 张卫刚
 * @Date Created on 2023/6/12
 */

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AlipayMiniProgramParam {

    private Long messageTemplateId;

    private Integer sendAccount;

    private Set<String> toUserId;

    private Map<String,String> data;
}
