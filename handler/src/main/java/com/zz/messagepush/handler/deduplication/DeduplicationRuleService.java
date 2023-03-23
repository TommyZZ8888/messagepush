package com.zz.messagepush.handler.deduplication;

import cn.hutool.core.date.DateUtil;
import com.zz.messagepush.common.domain.dto.TaskInfo;
import com.zz.messagepush.handler.domain.DeduplicationParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * @Description
 * @Author 张卫刚
 * @Date Created on 2023/3/23
 */

@Service
public class DeduplicationRuleService {


    @Autowired
    private ContentAbstractDeduplicationService contentAbstractDeduplicationService;

    @Autowired
    private FrequencyDeduplicationService frequencyDeduplicationService;

    public void duplication(TaskInfo taskInfo) {

        //文案去重
        DeduplicationParam build = DeduplicationParam.builder()
                .deduplicationTime(300L).countNum(1).taskInfo(taskInfo).build();
        contentAbstractDeduplicationService.deduplication(build);

        //运营总规则去重（一天内用户收到最多同一个渠道的消息次数）
        long seconds = (DateUtil.endOfDay(new Date()).getTime() - DateUtil.current()) / 1000;
        DeduplicationParam deduplicationParam = DeduplicationParam.builder()
                .deduplicationTime(seconds).countNum(5).taskInfo(taskInfo).build();
        frequencyDeduplicationService.deduplication(deduplicationParam);
    }
}
