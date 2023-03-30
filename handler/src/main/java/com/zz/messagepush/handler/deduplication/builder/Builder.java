package com.zz.messagepush.handler.deduplication.builder;

import com.zz.messagepush.common.domain.dto.TaskInfo;
import com.zz.messagepush.handler.domain.DeduplicationParam;

/**
 * @Description
 * @Author 张卫刚
 * @Date Created on 2023/3/28
 */
public interface Builder {


    String CONFIG_PRE = "deduplication_";

    /**
     * 根据配置构建去重参数
     * @param deduplication
     * @param taskInfo
     * @return
     */
    DeduplicationParam build(String deduplication, TaskInfo taskInfo);
}
