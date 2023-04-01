package com.zz.messagepush.cron.handler;

import com.xxl.job.core.handler.annotation.XxlJob;
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

    @XxlJob("austinJobHandler")
    public void execute() {
        log.info("xxl-job hello");
    }

}
