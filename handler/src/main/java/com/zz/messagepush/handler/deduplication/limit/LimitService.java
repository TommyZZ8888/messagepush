package com.zz.messagepush.handler.deduplication.limit;

import com.zz.messagepush.common.domain.dto.TaskInfo;
import com.zz.messagepush.handler.deduplication.AbstractDeduplicationService;
import com.zz.messagepush.handler.domain.DeduplicationParam;

import java.util.Set;

/**
 * @Description
 * @Author 张卫刚
 * @Date Created on 2023/4/19
 */
public interface LimitService {

    /**
     * 去重过滤
     * @param deduplicationService 去重器对象
     * @param taskInfo
     * @param param 去重参数
     * @return 返回不符合条件的号码
     */
    Set<String> limitFilter(AbstractDeduplicationService deduplicationService, TaskInfo taskInfo, DeduplicationParam param);
}
