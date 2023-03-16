package com.zz.messagepush.service.api.domain;


import com.zz.messagepush.service.api.domain.dto.MessageParam;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author DELL
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SendResponse {

    /**
     * 响应状态
     */
    private String code;

    /**
     * 响应编码
     */
    private String msg;
}
