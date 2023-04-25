package com.zz.messagepush.handler.domain.push;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.zz.messagepush.common.domain.dto.TaskInfo;

/**
 * @Description PushParam
 * @Author 张卫刚
 * @Date Created on 2023/4/25
 */

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PushParam {
    /**
     * 调用接口所需要的appid
     */
    private String appId;

    /**
     * 调用接口所需要的token
     */
    private String token;

    /**
     * 消息模板的信息
     */
    private TaskInfo taskInfo;
}
