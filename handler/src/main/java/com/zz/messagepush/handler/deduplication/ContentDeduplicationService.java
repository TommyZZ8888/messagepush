package com.zz.messagepush.handler.deduplication;

import cn.hutool.crypto.digest.DigestUtil;
import com.alibaba.fastjson.JSON;
import com.zz.messagepush.common.domain.dto.TaskInfo;
import com.zz.messagepush.common.enums.DeduplicationType;
import com.zz.messagepush.handler.deduplication.limit.LimitService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

/**
 * @Description 内容去重服务（默认5分钟相同的文案发给相同的用户去重）
 * @Author 张卫刚
 * @Date Created on 2023/3/23
 */
@Service
public class ContentDeduplicationService extends AbstractDeduplicationService {


    public ContentDeduplicationService(@Qualifier("SlideWindowLimitService")LimitService limitService) {
        this.limitService = limitService;
        deduplicationType = DeduplicationType.CONTENT.getCode();
    }

    /**
     * 内容去重
     * key: md5(templateId + receiver + content)
     * 相同的内容相同的模板短时间内发给同一个人
     *
     * @param taskInfo
     * @param receiver
     * @return
     */
    @Override
    public String deduplicationSingleKey(TaskInfo taskInfo, String receiver) {
        return DigestUtil.md5Hex(taskInfo.getMessageTemplateId() + receiver
                + JSON.toJSONString(taskInfo.getContentModel()));
    }
}
