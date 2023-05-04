package com.zz.messagepush.handler.domain.sms;

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

//    /**
//     * 发送账号
//     */
//    private Integer sendAccount;
}
