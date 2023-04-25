package com.zz.messagepush.handler.domain.push.getui;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.annotation.JSONField;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Description SendPushResult
 * @Author 张卫刚
 * @Date Created on 2023/4/23
 */

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SendPushResult {

    @JSONField(name = "msg")
    private String msg;

    @JSONField(name = "code")
    private Integer code;

    @JSONField(name = "data")
    private JSONObject data;

}
