package com.zz.messagepush.cron.utils;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.zz.messagepush.common.constant.AustinConstant;
import com.zz.messagepush.common.domain.ResponseResult;
import com.zz.messagepush.common.enums.RespStatusEnum;
import com.zz.messagepush.cron.constant.XxlJobConstant;
import com.zz.messagepush.cron.domain.entity.XxlJobGroup;
import com.zz.messagepush.cron.domain.entity.XxlJobInfo;
import com.zz.messagepush.cron.enums.*;
import com.zz.messagepush.cron.service.CronTaskService;
import com.zz.messagepush.support.domain.entity.MessageTemplateEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * @Description
 * @Author 张卫刚
 * @Date Created on 2023/3/31
 */
@Component
public class XxlJobUtils {

    @Value("${xxl.job.executor.appname}")
    private String appname;

    @Value("${xxl.job.executor.jobHandlerName}")
    private String jobHandlerName;

    @Autowired
    private CronTaskService cronTaskService;

    /**
     * 构建xxlJobInfo信息
     *
     * @param messageTemplate 是否启动定时任务
     * @return
     */
    public XxlJobInfo buildXxlJobInfo(MessageTemplateEntity messageTemplate) {

        // 判断是否为cron表达式
        String scheduleConf = messageTemplate.getExpectPushTime();
        //如果没有指定那个表达式立即执行 （给到则延迟）
        if (messageTemplate.getExpectPushTime().equals(String.valueOf(AustinConstant.FALSE))) {
            scheduleConf =DateUtil.format(DateUtil.offsetSecond(new Date(),XxlJobConstant.DELAY_TIME),AustinConstant.CRON_FORMAT);
        }

        XxlJobInfo xxlJobInfo = XxlJobInfo.builder().jobGroup(1).jobDesc(messageTemplate.getName())
                .author(messageTemplate.getCreator())
                .scheduleConf(scheduleConf)
                .scheduleType(ScheduleTypeEnum.CRON.name())
                .misfireStrategy(MisFireStrategyEnum.DO_NOTHING.name())
                .executorRouteStrategy(ExecutorRouteStrategyEnum.CONSISTENT_HASH.name())
                .executorHandler(XxlJobConstant.JOB_HANDLER_NAME)
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


    /**
     * 根据配置文件的内容获取groupId
     * 没有则创建
     *
     * @return
     */
    private Integer queryJobGroupId() {
        ResponseResult responseResult = cronTaskService.getGroupId(appname, jobHandlerName);
        if (responseResult.getData() == null) {
            XxlJobGroup xxlJobGroup = XxlJobGroup.builder().appName(appname).title(jobHandlerName).addressType(AustinConstant.FALSE).build();
            ResponseResult group = cronTaskService.createGroup(xxlJobGroup);
            if (RespStatusEnum.SUCCESS.getCode().equals(group.getCode())) {
                return (Integer) group.getData();
            }
        }
        return (Integer) responseResult.getData();
    }
}
