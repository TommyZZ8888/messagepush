package com.zz.messagepush.handler.deduplication.builder;

import cn.hutool.core.date.DateUtil;
import com.zz.messagepush.common.domain.dto.TaskInfo;
import com.zz.messagepush.common.enums.AnchorStateEnum;
import com.zz.messagepush.common.enums.DeduplicationType;
import com.zz.messagepush.handler.domain.DeduplicationParam;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * @Description
 * @Author 张卫刚
 * @Date Created on 2023/3/28
 */
@Service
public class FrequencyDeduplicationBuilder extends AbstractDeduplicationBuilder implements Builder {

    public FrequencyDeduplicationBuilder() {
        deduplicationType = DeduplicationType.FREQUENCY.getCode();
    }

    @Override
    public DeduplicationParam build(String deduplication, TaskInfo taskInfo) {
        DeduplicationParam deduplicationParam = getParamsFromConfig(deduplicationType, deduplication, taskInfo);
        if (deduplicationParam == null) {
            return null;
        }
        deduplicationParam.setDeduplicationTime((DateUtil.endOfDay(new Date()).getTime() - DateUtil.current()) / 1000);
        deduplicationParam.setAnchorStateEnum(AnchorStateEnum.RULE_DEDUPLICATION);
        return deduplicationParam;
    }
}
