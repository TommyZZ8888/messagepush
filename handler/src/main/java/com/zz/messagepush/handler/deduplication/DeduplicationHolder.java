package com.zz.messagepush.handler.deduplication;

import com.zz.messagepush.handler.deduplication.builder.Builder;
import com.zz.messagepush.handler.deduplication.builder.DeduplicationService;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * @Description
 * @Author 张卫刚
 * @Date Created on 2023/3/28
 */

@Service
public class DeduplicationHolder {

    private final Map<Integer, Builder> builderMap = new HashMap<>();

    private final Map<Integer, DeduplicationService> serviceMap = new HashMap<>();

    public Builder selectBuilder(Integer key) {
        return builderMap.get(key);
    }

    public void putBuilder(Integer key, Builder builder) {
        builderMap.put(key, builder);
    }

    public DeduplicationService selectService(Integer key) {
        return serviceMap.get(key);
    }

    public void putService(Integer key, DeduplicationService deduplicationService) {
        serviceMap.put(key, deduplicationService);
    }
}
