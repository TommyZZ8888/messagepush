package com.zz.messagepush.handler.deduplication.handler;

import com.zz.messagepush.handler.domain.DeduplicationParam;

/**
 * @Description
 * @Author 张卫刚
 * @Date Created on 2023/3/28
 */
public interface Builder {

    DeduplicationParam build(String deduplication, String key);
}
