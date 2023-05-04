package com.zz.messagepush.handler.domain.sms;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @Description YunPianSendResult
 * @Author 张卫刚
 * @Date Created on 2023/5/4
 */
@NoArgsConstructor
@Data
public class YunPianSendResult {


    @JSONField(name = "total_count")
    private Integer totalCount;
    @JSONField(name = "total_fee")
    private String totalFee;
    @JSONField(name = "unit")
    private String unit;
    @JSONField(name = "data")
    private List<DataDTO> data;

    @NoArgsConstructor
    @Data
    public static class DataDTO {
        @JSONField(name = "http_status_code")
        private Integer httpStatusCode;
        @JSONField(name = "code")
        private Integer code;
        @JSONField(name = "msg")
        private String msg;
        @JSONField(name = "count")
        private Integer count;
        @JSONField(name = "fee")
        private Integer fee;
        @JSONField(name = "unit")
        private String unit;
        @JSONField(name = "mobile")
        private String mobile;
        @JSONField(name = "sid")
        private String sid;
    }
}
