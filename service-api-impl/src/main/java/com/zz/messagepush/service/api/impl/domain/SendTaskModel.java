package com.zz.messagepush.service.api.impl.domain;

import com.zz.messagepush.common.domain.dto.TaskInfo;
import com.zz.messagepush.service.api.domain.dto.MessageParam;
import com.zz.messagepush.support.pipeline.ProcessModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @Description 发送消息任务模型
 * @Author 张卫刚
 * @Date Created on 2023/3/16
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SendTaskModel implements ProcessModel {

    @ApiModelProperty("请求类型 10single 20batch")
    private int requestType;

    @ApiModelProperty("请求参数 single接口")
    private MessageParam messageParam;

    @ApiModelProperty("请求参数 batch 接口")
    private List<MessageParam> messageParamList;

    @ApiModelProperty("发送任务信息")
    private TaskInfo taskInfo;

    private Long messageTemplateId;
}
