package com.zz.messagepush.handler.domain;

import com.zz.messagepush.common.domain.dto.TaskInfo;
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
    private Long deduplicationTime;

    /**
     * 去重需要达到的次数
     */
    private Integer countNum;
}
