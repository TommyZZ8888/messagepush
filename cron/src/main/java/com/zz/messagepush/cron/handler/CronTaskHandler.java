package com.zz.messagepush.cron.handler;

import com.alibaba.fastjson.JSONObject;
import com.xxl.job.core.context.XxlJobHelper;
import com.xxl.job.core.handler.annotation.XxlJob;
import com.zz.messagepush.support.domain.entity.MessageTemplateEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @Description
 * @Author 张卫刚
 * @Date Created on 2023/3/31
 */

@Service
@Slf4j
public class CronTaskHandler {

    @XxlJob("austinJob")
    public void execute() {
        MessageTemplateEntity messageTemplateEntity = JSONObject.parseObject(XxlJobHelper.getJobParam(), MessageTemplateEntity.class);
    }

}
