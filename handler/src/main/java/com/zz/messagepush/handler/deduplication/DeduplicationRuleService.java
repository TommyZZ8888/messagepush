package com.zz.messagepush.handler.deduplication;

import cn.hutool.core.date.DateUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.nacos.shaded.com.google.common.collect.Lists;
import com.ctrip.framework.apollo.Config;
import com.ctrip.framework.apollo.spring.annotation.ApolloConfig;
import com.zz.messagepush.common.constant.AustinConstant;
import com.zz.messagepush.common.domain.dto.TaskInfo;
import com.zz.messagepush.common.enums.AnchorStateEnum;
import com.zz.messagepush.common.utils.ApplicationContextUtil;
import com.zz.messagepush.handler.constants.DeduplicationConstants;
import com.zz.messagepush.handler.deduplication.handler.BuilderFactory;
import com.zz.messagepush.handler.deduplication.handler.DeduplicationService;
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
    private BuilderFactory builderFactory;

    /**
     * 配置样例：{"contentDeduplication":{"num":1,"time":300},"frequencyDeduplication":{"num":5}}
     */
    private static final String SERVICE = "Service";
    @ApolloConfig("boss.austin")
    private Config config;

    //需要去重的服务
    private static final List<String> DEDUPLICATION_LIST = Lists.newArrayList(DeduplicationConstants.CONTENT_DEDUPLICATION, DeduplicationConstants.FREQUENCY_DEDUPLICATION);

    public void duplication(TaskInfo taskInfo) {

        // 配置样例：{"contentDeduplication":{"num":1,"time":300},"frequencyDeduplication":{"num":5}}
        String deduplication = config.getProperty(DeduplicationConstants.DEDUPLICATION_RULE_KEY, AustinConstant.APOLLO_DEFAULT_VALUE_JSON_OBJECT);
        //去重
        DEDUPLICATION_LIST.forEach(
                key -> {
                    DeduplicationParam deduplicationParam = builderFactory.select(key).build(deduplication, key);
                    if (deduplicationParam != null) {
                        deduplicationParam.setTaskInfo(taskInfo);
                        DeduplicationService deduplicationService = ApplicationContextUtil.getBean(key + SERVICE,DeduplicationService.class);
                        deduplicationService.deduplication(deduplicationParam);
                    }
                }
        );



//        //文案去重
//        DeduplicationParam build = DeduplicationParam.builder()
//                .deduplicationTime(contentDeduplication.getLong(TIME))
//                .countNum(contentDeduplication.getInteger(NUM))
//                .anchorStateEnum(AnchorStateEnum.CONTENT_DEDUPLICATION)
//                .taskInfo(taskInfo).build();
//        contentAbstractDeduplicationService.deduplication(build);
//
//        //运营总规则去重（一天内用户收到最多同一个渠道的消息次数）
//        long seconds = (DateUtil.endOfDay(new Date()).getTime() - DateUtil.current()) / 1000;
//        DeduplicationParam deduplicationParam = DeduplicationParam.builder()
//                .deduplicationTime(seconds)
//                .countNum(frequencyDeduplication.getInteger(NUM))
//                .anchorStateEnum(AnchorStateEnum.RULE_DEDUPLICATION)
//                .taskInfo(taskInfo).build();
//        frequencyDeduplicationService.deduplication(deduplicationParam);
    }
}
