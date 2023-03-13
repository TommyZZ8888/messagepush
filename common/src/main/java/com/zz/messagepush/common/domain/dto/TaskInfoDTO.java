package com.zz.messagepush.common.domain.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;

import java.util.Set;

/**
 * @Description 发送任务信息
 * @Author 张卫刚
 * @Date Created on 2023/3/13
 */
@Data
@Builder
public class TaskInfoDTO {

    @ApiModelProperty("消息模板id")
    private Long messageTemplateId;

    @ApiModelProperty("业务id")
    private Long businessId;

    @ApiModelProperty("接收者")
    private Set<String> receiver;

    @ApiModelProperty("发送的id类型")
    private Integer idType;

    @ApiModelProperty("发送渠道")
    private Integer sendChannel;

    @ApiModelProperty("模板类型")
    private Integer templateType;

    @ApiModelProperty("消息类型")
    private Integer msgType;

    @ApiModelProperty("消息内容")
    private String content;

    @ApiModelProperty("发送账号")
    private Integer sendAccount;

    @ApiModelProperty("消息去重时间 单位/小时")
    private Integer deduplicationTime;

    @ApiModelProperty("消息模板id")
    private Integer isNightShield;


}
