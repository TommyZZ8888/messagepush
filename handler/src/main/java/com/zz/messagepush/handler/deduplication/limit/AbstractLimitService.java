package com.zz.messagepush.handler.deduplication.limit;

import com.zz.messagepush.common.domain.dto.TaskInfo;
import com.zz.messagepush.handler.deduplication.AbstractDeduplicationService;

import java.util.ArrayList;
import java.util.List;

/**
 * @Description
 * @Author 张卫刚
 * @Date Created on 2023/4/19
 */
public abstract class AbstractLimitService implements LimitService {

    protected List<String> deduplicationAllKey(AbstractDeduplicationService service, TaskInfo taskInfo) {
        List<String> result = new ArrayList<>(taskInfo.getReceiver().size());
        for (String receiver : taskInfo.getReceiver()) {
            String singleKey = deduplicationSingleKey(service, taskInfo, receiver);
            result.add(singleKey);
        }
        return result;
    }

    protected String deduplicationSingleKey(AbstractDeduplicationService service, TaskInfo taskInfo, String receiver) {
        return service.deduplicationSingleKey(taskInfo, receiver);
    }
}
