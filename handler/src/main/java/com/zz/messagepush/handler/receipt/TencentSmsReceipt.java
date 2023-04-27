package com.zz.messagepush.handler.receipt;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;
import com.alibaba.nacos.shaded.com.google.common.base.Throwables;
import com.tencentcloudapi.common.Credential;
import com.tencentcloudapi.common.exception.TencentCloudSDKException;
import com.tencentcloudapi.common.profile.ClientProfile;
import com.tencentcloudapi.common.profile.HttpProfile;
import com.tencentcloudapi.sms.v20210111.SmsClient;
import com.tencentcloudapi.sms.v20210111.models.PullSmsSendStatus;
import com.tencentcloudapi.sms.v20210111.models.PullSmsSendStatusRequest;
import com.tencentcloudapi.sms.v20210111.models.PullSmsSendStatusResponse;
import com.zz.messagepush.common.constant.SendAccountConstant;
import com.zz.messagepush.common.domain.dto.account.TencentSmsAccount;
import com.zz.messagepush.common.enums.SmsStatus;
import com.zz.messagepush.support.config.SupportThreadPoolConfig;
import com.zz.messagepush.support.domain.entity.SmsRecordEntity;
import com.zz.messagepush.support.mapper.SmsRecordMapper;
import com.zz.messagepush.support.utils.AccountUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @Description 拉取短信回执信息
 * @Author 张卫刚
 * @Date Created on 2023/4/27
 */

@Component
@Slf4j
public class TencentSmsReceipt {

    @Autowired
    private AccountUtils accountUtils;

    @Autowired
    private SmsRecordMapper smsRecordMapper;

    @PostConstruct
    public void init() {
        TencentSmsAccount account = accountUtils.getAccount(10, SendAccountConstant.SMS_ACCOUNT_KEY, SendAccountConstant.SMS_ACCOUNT_PREFIX, TencentSmsAccount.class);
        SupportThreadPoolConfig.getPendingSingleThreadPool().execute(() -> {
            while (true) {
                try {
                    SmsClient smsClient = getSmsClient(account);
                    PullSmsSendStatusRequest req = new PullSmsSendStatusRequest();
                    req.setLimit(10L);
                    req.setSmsSdkAppId(account.getSmsSdkAppId());

                    PullSmsSendStatusResponse resp = smsClient.PullSmsSendStatus(req);

                    List<SmsRecordEntity> smsRecordList = new ArrayList<>();
                    if (resp != null && resp.getPullSmsSendStatusSet() != null && resp.getPullSmsSendStatusSet().length > 0) {
                        for (PullSmsSendStatus pullSmsSendStatus : resp.getPullSmsSendStatusSet()) {
                            SmsRecordEntity smsRecord = SmsRecordEntity.builder()
                                    .sendDate(new Date())
                                    .messageTemplateId(0L)
                                    .phone(Long.valueOf(pullSmsSendStatus.getSubscriberNumber()))
                                    .supplierId(account.getSupplierId())
                                    .supplierName(account.getSupplierName())
                                    .msgContent("")
                                    .seriesId(pullSmsSendStatus.getSerialNo())
                                    .chargingNum(0)
                                    .status("SUCCESS".equals(pullSmsSendStatus.getReportStatus()) ? SmsStatus.RECEIVE_SUCCESS.getCode() : SmsStatus.RECEIVE_FAIL.getCode())
                                    .reportContent(pullSmsSendStatus.getDescription())
                                    .created(DateUtil.date(pullSmsSendStatus.getUserReceiveTime()))
                                    .updated(new Date())
                                    .build();
                            smsRecordList.add(smsRecord);
                        }
                    }
                    if (CollUtil.isNotEmpty(smsRecordList)) {
                        smsRecordMapper.saveAll(smsRecordList);
                    }
                    Thread.sleep(200);
                } catch (Exception e) {
                    log.error("tencentSmsReceipt#init fail:{}", Throwables.getStackTraceAsString(e));
                }
            }
        });
    }

    /**
     * 构造smsClient
     *
     * @param account
     * @return
     */
    private SmsClient getSmsClient(TencentSmsAccount account) {
        Credential credential = new Credential(account.getSecretId(), account.getSecretKey());
        HttpProfile httpProfile = new HttpProfile();
        httpProfile.setEndpoint(account.getUrl());
        ClientProfile clientProfile = new ClientProfile();
        clientProfile.setHttpProfile(httpProfile);
        return new SmsClient(credential, account.getRegion(), clientProfile);
    }

}
