package com.zz.messagepush.service.api.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.Map;


/**
 * @author DELL
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
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
