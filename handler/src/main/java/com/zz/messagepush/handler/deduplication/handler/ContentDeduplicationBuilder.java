package com.zz.messagepush.handler.deduplication.handler;

import com.alibaba.fastjson.JSONObject;
import com.zz.messagepush.common.domain.dto.TaskInfo;
import com.zz.messagepush.common.enums.AnchorStateEnum;
import com.zz.messagepush.common.enums.DeduplicationType;
import com.zz.messagepush.handler.domain.DeduplicationParam;
import org.springframework.stereotype.Service;

/**
 * @Description
 * @Author 张卫刚
 * @Date Created on 2023/3/28
 */
@Service
public class ContentDeduplicationBuilder extends AbstractDeduplicationBuilder implements Builder {

    public ContentDeduplicationBuilder() {
        deduplicationType = DeduplicationType.CONTENT.getCode();
    }

    @Override
    public DeduplicationParam build(String deduplication, TaskInfo taskInfo) {
        DeduplicationParam deduplicationParam = getParamsFromConfig(deduplicationType, deduplication, taskInfo);
        if (deduplicationParam == null) {
            return null;
        }
        deduplicationParam.setAnchorStateEnum(AnchorStateEnum.CONTENT_DEDUPLICATION);
        return deduplicationParam;

    }
}
