package com.zz.messagepush.handler.deduplication;

import cn.hutool.core.date.DateUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.ctrip.framework.apollo.Config;
import com.ctrip.framework.apollo.spring.annotation.ApolloConfig;
import com.zz.messagepush.common.constant.AustinConstant;
import com.zz.messagepush.common.domain.dto.TaskInfo;
import com.zz.messagepush.common.enums.AnchorStateEnum;
import com.zz.messagepush.handler.domain.DeduplicationParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * @Description 去重服务
 * @Author 张卫刚
 * @Date Created on 2023/3/23
 */

@Service
public class DeduplicationRuleService {


    @Autowired
    private ContentDeduplicationService contentAbstractDeduplicationService;

    @Autowired
    private FrequencyDeduplicationService frequencyDeduplicationService;

    /**
     * 配置样例：{"contentDeduplication":{"num":1,"time":300},"frequencyDeduplication":{"num":5}}
     */
    private static final String DEDUPLICATION_RULE_KEY = "deduplication";
    private static final String CONTENT_DEDUPLICATION = "contentDeduplication";
    private static final String FREQUENCY_DEDUPLICATION = "frequencyDeduplication";
    private static final String TIME = "time";
    private static final String NUM = "num";

    @ApolloConfig("boss.austin")
    private Config config;

    public void duplication(TaskInfo taskInfo) {
        JSONObject property = JSON.parseObject(config.getProperty(DEDUPLICATION_RULE_KEY, AustinConstant.APOLLO_DEFAULT_VALUE_JSON_OBJECT));
        JSONObject contentDeduplication = property.getJSONObject(CONTENT_DEDUPLICATION);
        JSONObject frequencyDeduplication = property.getJSONObject(FREQUENCY_DEDUPLICATION);

        //文案去重
        DeduplicationParam build = DeduplicationParam.builder()
                .deduplicationTime(contentDeduplication.getLong(TIME))
                .countNum(contentDeduplication.getInteger(NUM))
                .anchorStateEnum(AnchorStateEnum.CONTENT_DEDUPLICATION)
                .taskInfo(taskInfo).build();
        contentAbstractDeduplicationService.deduplication(build);

        //运营总规则去重（一天内用户收到最多同一个渠道的消息次数）
        long seconds = (DateUtil.endOfDay(new Date()).getTime() - DateUtil.current()) / 1000;
        DeduplicationParam deduplicationParam = DeduplicationParam.builder()
                .deduplicationTime(seconds)
                .countNum(frequencyDeduplication.getInteger(NUM))
                .anchorStateEnum(AnchorStateEnum.RULE_DEDUPLICATION)
                .taskInfo(taskInfo).build();
        frequencyDeduplicationService.deduplication(deduplicationParam);
    }
}
