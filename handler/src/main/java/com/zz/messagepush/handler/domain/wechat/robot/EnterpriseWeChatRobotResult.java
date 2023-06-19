package com.zz.messagepush.handler.domain.wechat.robot;

import com.alibaba.fastjson.annotation.JSONField;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Description EnterpriseWeChatRobotResult
 * @Author 张卫刚
 * @Date Created on 2023/6/19
 */

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EnterpriseWeChatRobotResult {

    @JSONField(name = "errcode")
    private Integer errcode;
    @JSONField(name = "errmsg")
    private String errmsg;
    @JSONField(name = "type")
    private String type;
    @JSONField(name = "media_id")
    private String mediaId;
    @JSONField(name = "created_at")
    private String createdAt;
}
