package com.zz.messagepush.common.domain;

import lombok.Builder;
import lombok.Data;

import java.util.Set;


@Data
@Builder
public class SmsParam {

    /**
     * 业务id
     */
    private Long messageTemplateId;

    /**
     * 发送号码
     */
    private Set<String> phones;

    /**
     * 消息内容
     */
    private String content;

    /**
     * 渠道商id
     */
    private Integer supplierId;

    /**
     * 渠道商名字
     */
    private String supplierName;
}
