package com.zz.messagepush.common.domain.dto;

import com.zz.messagepush.common.domain.dto.model.ContentModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

/**
 * @Description 发送任务信息
 * @Author 张卫刚
 * @Date Created on 2023/3/13
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TaskInfo {

    @ApiModelProperty("消息模板id")
    private Long messageTemplateId;

    /**
     * 数据追踪使用
     */
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

    /**
     * 发送文案模型
     * messageTemplate 表存储的content是JSON（所有的内容都会塞进去）
     * 不同渠道要发送的内容不一样（push会有img，而短信没有）
     * 所以会有ContentModel
     */
    @ApiModelProperty("消息内容")
    private ContentModel contentModel;

    @ApiModelProperty("发送账号")
    private Integer sendAccount;

    @ApiModelProperty("消息去重时间 单位/小时")
    private Integer deduplicationTime;

    @ApiModelProperty("消息模板id")
    private Integer isNightShield;

    @ApiModelProperty("屏蔽类型")
    private Integer shieldType;


}
