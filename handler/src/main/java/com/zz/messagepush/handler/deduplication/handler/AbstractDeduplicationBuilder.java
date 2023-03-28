package com.zz.messagepush.handler.deduplication.handler;

import com.alibaba.fastjson.JSONObject;
import com.zz.messagepush.common.domain.dto.TaskInfo;
import com.zz.messagepush.handler.deduplication.DeduplicationHolder;
import com.zz.messagepush.handler.domain.DeduplicationParam;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;

/**
 * @Description
 * @Author 张卫刚
 * @Date Created on 2023/3/28
 */
public abstract class AbstractDeduplicationBuilder implements Builder {


    protected Integer deduplicationType;

    @Autowired
    private DeduplicationHolder deduplicationHolder;


    @PostConstruct
    public void init() {
        deduplicationHolder.putBuilder(deduplicationType, this);
    }

    public DeduplicationParam getParamsFromConfig(Integer key, String deduplicationConfig, TaskInfo taskInfo) {
        JSONObject jsonObject = JSONObject.parseObject(deduplicationConfig);
        if (jsonObject == null) {
            return null;
        }
        DeduplicationParam deduplicationParam = JSONObject.parseObject(jsonObject.getString(CONFIG_PRE + key), DeduplicationParam.class);
        if (deduplicationParam == null) {
            return null;
        }
        deduplicationParam.setTaskInfo(taskInfo);
        return deduplicationParam;
    }

}
