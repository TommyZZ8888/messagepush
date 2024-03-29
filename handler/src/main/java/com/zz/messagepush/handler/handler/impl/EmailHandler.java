package com.zz.messagepush.handler.handler.impl;

import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.mail.MailAccount;
import cn.hutool.extra.mail.MailUtil;
import com.alibaba.nacos.shaded.com.google.common.base.Throwables;
import com.google.common.util.concurrent.RateLimiter;
import com.sun.mail.util.MailSSLSocketFactory;
import com.zz.messagepush.common.constant.SendAccountConstant;
import com.zz.messagepush.common.domain.dto.model.EmailContentModel;
import com.zz.messagepush.common.domain.dto.TaskInfo;
import com.zz.messagepush.common.enums.ChannelType;
import com.zz.messagepush.handler.enums.RateLimitStrategy;
import com.zz.messagepush.handler.flowcontrol.FlowControlParam;
import com.zz.messagepush.handler.handler.BaseHandler;
import com.zz.messagepush.handler.handler.Handler;
import com.zz.messagepush.support.domain.entity.MessageTemplateEntity;
import com.zz.messagepush.support.utils.AccountUtils;
import com.zz.messagepush.support.utils.AustinFileUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import java.io.File;
import java.security.GeneralSecurityException;

/**
 * @Description 邮件发送处理
 * @Author 张卫刚
 * @Date Created on 2023/3/22
 */

@Configuration
@Slf4j
public class EmailHandler extends BaseHandler implements Handler {

    @Autowired
    private AccountUtils accountUtils;

    @Value("${austin.business.upload.crowd.path}")
    private String dataPath;


    public EmailHandler() {
        channelCode = ChannelType.EMAIL.getCode();

        double rateInitValue = 3;
        flowControlParam = FlowControlParam.builder().rateInitValue(rateInitValue)
                .rateLimitStrategy(RateLimitStrategy.REQUEST_RATE_LIMIT)
                .rateLimiter(RateLimiter.create(rateInitValue)).build();
    }


    @Override
    public boolean handler(TaskInfo taskInfo) {
        EmailContentModel contentModel = (EmailContentModel) taskInfo.getContentModel();
        MailAccount account = getAccountConfig(taskInfo.getSendAccount());

        try {
            File file = StrUtil.isNotBlank(contentModel.getUrl()) ? AustinFileUtil.getRemoteUrl2File(dataPath, contentModel.getUrl()) : null;
            MailUtil.send(account, taskInfo.getReceiver(), contentModel.getTitle(), contentModel.getContent(), true, file);
        } catch (Exception e) {
            log.error("EmailHandler#handler fail!{},params:{}", Throwables.getStackTraceAsString(e), taskInfo);
            return false;
        }
        return true;
    }

    private MailAccount getAccountConfig(Integer sendAccount) {
        MailAccount account = accountUtils.getAccountById(sendAccount, MailAccount.class);

        try {
            account.setHost("smtp.qq.com").setPort(465);
            account.setUser("403686131@qq.com").setPass("//TODO").setAuth(true);
            account.setFrom("403686131@qq.com");

            MailSSLSocketFactory sf = new MailSSLSocketFactory();
            sf.setTrustAllHosts(true);
            account.setAuth(account.isAuth()).setStarttlsEnable(account.isStarttlsEnable()).setSslEnable(account.isSslEnable()).setCustomProperty("mail.smtp.ssl.socketFactory", sf);

            account.setTimeout(25000).setConnectionTimeout(25000);
        } catch (GeneralSecurityException e) {
            log.error("EmailHandler#getAccount fail!{}", Throwables.getStackTraceAsString(e));
        }
        return account;
    }

    @Override
    public void recall(MessageTemplateEntity messageTemplate) {

    }
}
