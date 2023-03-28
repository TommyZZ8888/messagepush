package com.zz.messagepush.handler.deduplication;

import com.ctrip.framework.apollo.Config;
import com.ctrip.framework.apollo.spring.annotation.ApolloConfig;
import com.zz.messagepush.common.constant.AustinConstant;
import com.zz.messagepush.common.domain.dto.TaskInfo;
import com.zz.messagepush.common.enums.DeduplicationType;
import com.zz.messagepush.handler.domain.DeduplicationParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * @Description 去重服务
 * @Author 张卫刚
 * @Date Created on 2023/3/23
 */

@Service
public class DeduplicationRuleService {

    @Autowired
    private DeduplicationHolder deduplicationHolder;

    /**
     * 配置样例：{"contentDeduplication":{"num":1,"time":300},"frequencyDeduplication":{"num":5}}
     */
    private static final String DEDUPLICATION_RULE_KEY = "deduplication";
    @ApolloConfig("boss.austin")
    private Config config;

    //需要去重的服务

    public void duplication(TaskInfo taskInfo) {

        // 配置样例：{"contentDeduplication":{"num":1,"time":300},"frequencyDeduplication":{"num":5}}
        String deduplication = config.getProperty(DEDUPLICATION_RULE_KEY, AustinConstant.APOLLO_DEFAULT_VALUE_JSON_OBJECT);
        //去重
        List<Integer> deduplicationList = DeduplicationType.getDeduplicationList();
        for (Integer deduplicationType : deduplicationList) {
            DeduplicationParam deduplicationParam = deduplicationHolder.selectBuilder(deduplicationType).build(deduplication, taskInfo);
            if (deduplicationParam != null) {
                deduplicationHolder.selectService(deduplicationType).deduplication(deduplicationParam);
            }
        }
    }
}
