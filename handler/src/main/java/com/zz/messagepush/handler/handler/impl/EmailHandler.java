package com.zz.messagepush.handler.handler.impl;

import cn.hutool.extra.mail.MailAccount;
import cn.hutool.extra.mail.MailUtil;
import com.alibaba.nacos.shaded.com.google.common.base.Throwables;
import com.sun.mail.util.MailSSLSocketFactory;
import com.zz.messagepush.common.domain.dto.EmailContentModel;
import com.zz.messagepush.common.domain.dto.TaskInfo;
import com.zz.messagepush.common.enums.ChannelType;
import com.zz.messagepush.handler.handler.Handler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;

import java.security.GeneralSecurityException;

/**
 * @Description 邮件发送处理
 * @Author 张卫刚
 * @Date Created on 2023/3/22
 */

@Configuration
@Slf4j
public class EmailHandler extends Handler {


    public EmailHandler() {
        channelCode = ChannelType.EMAIL.getCode();
    }


    @Override
    public boolean handler(TaskInfo taskInfo) {
        EmailContentModel contentModel = (EmailContentModel) taskInfo.getContentModel();
        MailAccount account = getAccount();

        try {
            MailUtil.send(account, taskInfo.getReceiver(), contentModel.getTitle(), contentModel.getContent(), true, null);
        } catch (Exception e) {
            log.error("EmailHandler#handler fail!{},params:{}", Throwables.getStackTraceAsString(e), taskInfo);
            return false;
        }
        return true;
    }

    private MailAccount getAccount() {
        MailAccount account = new MailAccount();

        try {
            account.setHost("smtp.qq.com").setPort(465);
            account.setUser("403686131@qq.com").setPass("cmnznhomnbtlbggi").setAuth(true);
            account.setFrom("403686131@qq.com");

            MailSSLSocketFactory sf = new MailSSLSocketFactory();
            sf.setTrustAllHosts(true);
            account.setStarttlsEnable(true).setSslEnable(true).setCustomProperty("mail.smtp.ssl.socketFactory", sf);

            account.setTimeout(25000).setConnectionTimeout(25000);
        } catch (GeneralSecurityException e) {
            log.error("EmailHandler#getAccount fail!{}", Throwables.getStackTraceAsString(e));
        }
        return account;
    }
}
