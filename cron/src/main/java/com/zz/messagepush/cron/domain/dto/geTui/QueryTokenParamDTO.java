package com.zz.messagepush.cron.domain.dto.geTui;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Description 请求个推token时的参数
 * @Author 张卫刚
 * @Date Created on 2023/4/23
 */

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class QueryTokenParamDTO {

    @JSONField(name = "sign")
    private String sign;

    @JSONField(name = "timestamp")
    private String timestamp;

    @JSONField(name = "appkey")
    private String appKey;

}
