package com.zz.messagepush.handler.deduplication;

import cn.hutool.core.util.StrUtil;
import com.zz.messagepush.common.domain.dto.TaskInfo;
import com.zz.messagepush.common.enums.DeduplicationType;
import org.springframework.stereotype.Service;

/**
 * @Description 频次去重服务
 * @Author 张卫刚
 * @Date Created on 2023/3/23
 */

@Service
public class FrequencyDeduplicationService extends AbstractDeduplicationService {


    private static final String PREFIX = "FRE";

    public FrequencyDeduplicationService() {
        deduplicationType = DeduplicationType.FREQUENCY.getCode();
    }

    /**
     * 业务规则去重 构建key
     * key：receiver + templateId + sendChannel
     * 一天内一个用户只能收到某个渠道的消息N次
     *
     * @param taskInfo
     * @param receiver
     * @return
     */
    @Override
    protected String deduplicationSingleKey(TaskInfo taskInfo, String receiver) {
        return PREFIX + StrUtil.C_UNDERLINE
                + receiver + StrUtil.C_UNDERLINE
                + taskInfo.getMessageTemplateId() + StrUtil.C_UNDERLINE
                + taskInfo.getSendChannel();
    }
}
