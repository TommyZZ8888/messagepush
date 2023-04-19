package com.zz.messagepush.handler.deduplication.limit;

import com.zz.messagepush.common.domain.dto.TaskInfo;
import com.zz.messagepush.handler.deduplication.AbstractDeduplicationService;
import com.zz.messagepush.handler.domain.DeduplicationParam;
import com.zz.messagepush.support.utils.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Set;

/**
 * @Description
 * @Author 张卫刚
 * @Date Created on 2023/4/19
 */

@Service("SlideWindowLimitService")
public class SlideWindowLimitService extends AbstractLimitService{

    private static final String LIMIT_TAG = "SW_";

    @Autowired
    private RedisUtil redisUtil;

    @Override
    public Set<String> limitFilter(AbstractDeduplicationService deduplicationService, TaskInfo taskInfo, DeduplicationParam param) {
        return null;
    }
}
