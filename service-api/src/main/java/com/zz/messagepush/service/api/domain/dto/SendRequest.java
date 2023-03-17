package com.zz.messagepush.service.api.domain.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * @author DELL
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Accessors(chain = true)
public class SendRequest {

    /**
     * 执行业务类型
     * send 发送消息
     * recall 撤回消息
     */
    private String code;

    /**
     * 消息模板id
     */
    private Long messageTemplateId;

    /**
     * 消息相关的参数
     * 业务为send时，必传
     */
    private MessageParam messageParam;
}
