package com.zz.messagepush.cron.utils;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.zz.messagepush.common.constant.AustinConstant;
import com.zz.messagepush.cron.constant.XxlJobConstant;
import com.zz.messagepush.cron.domain.entity.XxlJobInfo;
import com.zz.messagepush.cron.enums.*;
import com.zz.messagepush.support.domain.entity.MessageTemplateEntity;

/**
 * @Description
 * @Author 张卫刚
 * @Date Created on 2023/3/31
 */
public class XxlJobUtils {

    /**
     * 构建xxlJobInfo信息
     *
     * @param messageTemplate
     * @param triggerStatus 是否启动定时任务
     * @return
     */
    public static XxlJobInfo buildXxlJobInfo(MessageTemplateEntity messageTemplate) {

        // 判断是否为cron表达式
        String scheduleConf = StrUtil.EMPTY;
        String scheduleType = ScheduleTypeEnum.NONE.name();
        if (!messageTemplate.getExpectPushTime().equals(String.valueOf(AustinConstant.FALSE))) {
            scheduleType = ScheduleTypeEnum.CRON.name();
            scheduleConf = messageTemplate.getExpectPushTime();
        }

        XxlJobInfo xxlJobInfo = XxlJobInfo.builder().jobGroup(1).jobDesc(messageTemplate.getName())
                .author(messageTemplate.getCreator())
                .scheduleConf(scheduleConf)
                .scheduleType(scheduleType)
                .misfireStrategy(MisFireStrategyEnum.DO_NOTHING.name())
                .executorBlockStrategy(ExecutorRouteStrategyEnum.CONSISTENT_HASH.name())
                .executorHandler(XxlJobConstant.HANDLER_NAME)
                .executorParam(JSON.toJSONString(messageTemplate))
                .executorBlockStrategy(ExecutorBlockStrategyEnum.SERIAL_EXECUTION.name())
                .executorTimeout(XxlJobConstant.TIME_OUT)
                .executorFailRetryCount(XxlJobConstant.RETRY_TIME)
                .glueType(GlueTypeEnum.BEAN.name())
                .triggerStatus(AustinConstant.FALSE)
                .glueRemark(StrUtil.EMPTY)
                .glueSource(StrUtil.EMPTY)
                .alarmEmail(StrUtil.EMPTY)
//                .childJobId(StrUtil.EMPTY)
                .build();

        if (messageTemplate.getCronTaskId() != null) {
            xxlJobInfo.setId(messageTemplate.getCronTaskId());
        }
        return xxlJobInfo;
    }
}
