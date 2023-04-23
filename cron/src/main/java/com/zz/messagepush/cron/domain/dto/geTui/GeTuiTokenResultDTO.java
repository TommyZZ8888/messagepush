package com.zz.messagepush.cron.domain.dto.geTui;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Description GeTuiTokenResultDTO
 * @Author 张卫刚
 * @Date Created on 2023/4/23
 */

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GeTuiTokenResultDTO {

    @JSONField(name = "msg")
    private String msg;

    @JSONField(name = "code")
    private Integer code;

    @JSONField(name = "data")
    public DataDTO data;

    @Data
    @NoArgsConstructor
   public class DataDTO {

        @JSONField(name = "expire_time")
        public String expireTime;

        @JSONField(name = "token")
        private String token;
    }
}
