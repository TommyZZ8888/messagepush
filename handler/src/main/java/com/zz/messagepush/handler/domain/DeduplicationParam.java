package com.zz.messagepush.handler.domain;

import com.alibaba.fastjson.annotation.JSONField;
import com.zz.messagepush.common.domain.dto.TaskInfo;
import com.zz.messagepush.common.enums.AnchorStateEnum;
import lombok.Builder;
import lombok.Data;

/**
 * @Description 去重服务所需要的参数
 * @Author 张卫刚
 * @Date Created on 2023/3/23
 */

@Data
@Builder
public class DeduplicationParam {


    /**
     * taskInfo 信息
     */
    private TaskInfo taskInfo;

    /**
     * 去重时间 单位：s
     */
    @JSONField(name = "time")
    private Long deduplicationTime;

    /**
     * 去重需要达到的次数
     */
    @JSONField(name = "num")
    private Integer countNum;


    /**
     * 标识哪种方式去重
     */
    private AnchorStateEnum anchorStateEnum;
}
