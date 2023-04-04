package com.zz.messagepush.cron.handler;

import com.alibaba.fastjson.JSONObject;
import com.xxl.job.core.context.XxlJobHelper;
import com.xxl.job.core.handler.annotation.XxlJob;
import com.zz.messagepush.cron.service.TaskHandler;
import com.zz.messagepush.support.domain.entity.MessageTemplateEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Description
 * @Author 张卫刚
 * @Date Created on 2023/3/31
 */

@Service
@Slf4j
public class CronTaskHandler {

    @Autowired
    private TaskHandler taskHandler;

    @XxlJob("austinJob")
    public void execute() {
        Long messageTemplateId = Long.valueOf(XxlJobHelper.getJobParam());
        taskHandler.handler(messageTemplateId);
    }

}
