package com.zz.messagepush.service.api.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MessageParam {


    /**
     * 接收者
     * 必传
     *
     */
    private String receiver;

    /**
     * 消息内容中的可变部分
     */
    private Map<String,String> variables;

    /**
     * 扩展参数
     */
    private Map<String,String> extra;


}
