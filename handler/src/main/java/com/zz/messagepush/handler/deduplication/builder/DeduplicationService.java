package com.zz.messagepush.handler.deduplication.builder;

import com.zz.messagepush.handler.domain.DeduplicationParam;

/**
 * @Description
 * @Author 张卫刚
 * @Date Created on 2023/3/28
 */
public interface DeduplicationService {

    void deduplication(DeduplicationParam param);
}
