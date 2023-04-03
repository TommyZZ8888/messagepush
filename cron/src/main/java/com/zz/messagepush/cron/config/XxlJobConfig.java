package com.zz.messagepush.cron.config;

import com.xxl.job.core.executor.impl.XxlJobSpringExecutor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Description
 * @Author 张卫刚
 * @Date Created on 2023/3/31
 */
@Slf4j
@Configuration
public class XxlJobConfig {

    @Value("${xxl.job.admin.addresses}")
    private String address;

    @Value("${xxl.job.admin.username}")
    private String username;

    @Value("${xxl.job.admin.password}")
    private String password;

    @Value("${xxl.job.executor.appname}")
    private String appName;

    @Value("${xxl.job.executor.ip}")
    private String ip;

    @Value("${xxl.job.executor.port}")
    private int port;

    @Value("${xxl.job.executor.logpath}")
    private String logPath;

    @Value("${xxl.job.executor.logretentiondays}")
    private int logRetentionDays;

    @Value("${xxl.job.accessToken}")
    private String accessToken;


    @Bean
    public XxlJobSpringExecutor xxlJobExecutor(){
        //创建xxlJobSpringExecutor执行器
        XxlJobSpringExecutor executor = new XxlJobSpringExecutor();
        executor.setAddress(address);
        executor.setIp(ip);
        executor.setAccessToken(accessToken);
        executor.setPort(port);
        executor.setLogPath(logPath);
        executor.setLogRetentionDays(logRetentionDays);
        return executor;
    }


}
