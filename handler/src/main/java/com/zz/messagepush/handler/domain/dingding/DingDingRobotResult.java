package com.zz.messagepush.handler.domain.dingding;

import com.google.gson.annotations.SerializedName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Description
 * @Author 张卫刚
 * @Date Created on 2023/4/17
 */

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DingDingRobotResult {

    @SerializedName("errCode")
    private Integer errCode;

    @SerializedName("errMsg")
    private String errMsg;
}
