package com.zz.messagepush.cron.handler;

import com.dtp.core.thread.DtpExecutor;
import com.xxl.job.core.context.XxlJobHelper;
import com.xxl.job.core.handler.annotation.XxlJob;
import com.zz.messagepush.cron.config.CronAsyncThreadPoolConfig;
import com.zz.messagepush.cron.service.TaskHandler;
import com.zz.messagepush.support.utils.ThreadPoolUtils;
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

    @Autowired
    private ThreadPoolUtils threadPoolUtils;

    private final DtpExecutor dtpExecutor = CronAsyncThreadPoolConfig.getXxlCronExecutor();

    @XxlJob("austinJob")
    public void execute() {
        Long messageTemplateId = Long.valueOf(XxlJobHelper.getJobParam());
        log.info("CronTaskHandler#execute messageTemplateId:{} cron exec!", XxlJobHelper.getJobParam());
        threadPoolUtils.registry(dtpExecutor);
        dtpExecutor.execute(() -> taskHandler.handler(messageTemplateId));
    }

}
