package com.zz.messagepush.handler.domain.sms;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;


/**
 * @Description LinTongSendResult
 * @Author 张卫刚
 * @Date Created on 2023/7/4
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LinTongSendResult {

    Integer code;

    String message;
    @JSONField(name = "data")
    List<DataDTO> dataDTOS;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public class DataDTO {
        Integer code;

        String message;

        Long msgId;

        String phone;
    }
}
