package com.zz.messagepush.handler.receipt;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.nacos.shaded.com.google.common.base.Throwables;
import com.tencentcloudapi.common.Credential;
import com.tencentcloudapi.common.profile.ClientProfile;
import com.tencentcloudapi.common.profile.HttpProfile;
import com.tencentcloudapi.sms.v20210111.SmsClient;
import com.tencentcloudapi.sms.v20210111.models.PullSmsSendStatus;
import com.tencentcloudapi.sms.v20210111.models.PullSmsSendStatusRequest;
import com.tencentcloudapi.sms.v20210111.models.PullSmsSendStatusResponse;
import com.zz.messagepush.common.constant.AustinConstant;
import com.zz.messagepush.common.domain.dto.account.TencentSmsAccount;
import com.zz.messagepush.common.domain.dto.account.YunPianSmsAccount;
import com.zz.messagepush.common.enums.ChannelType;
import com.zz.messagepush.common.enums.SmsStatus;
import com.zz.messagepush.common.enums.SmsSupplier;
import com.zz.messagepush.support.domain.entity.ChannelAccountEntity;
import com.zz.messagepush.support.domain.entity.SmsRecordEntity;
import com.zz.messagepush.support.mapper.ChannelAccountMapper;
import com.zz.messagepush.support.mapper.SmsRecordMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

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
public class SmsPullReceipt {

    @Autowired
    private ChannelAccountMapper channelAccountMapper;

    @Autowired
    private SmsRecordMapper smsRecordMapper;

    public void pull() {
        List<ChannelAccountEntity> accountEntities = channelAccountMapper.findAllByIsDeletedEqualsAndSendChannelEquals(AustinConstant.FALSE, ChannelType.SMS.getCode());
        for (ChannelAccountEntity accountEntity : accountEntities) {
            Integer supplierId = JSON.parseObject(accountEntity.getAccountConfig()).getInteger("supplierId");
            if (SmsSupplier.TENCENT.getCode().equals(supplierId)) {
                TencentSmsAccount account = JSON.parseObject(accountEntity.getAccountConfig(), TencentSmsAccount.class);
                pullTencent(account);
            } else if (SmsSupplier.YUN_PIAN.getCode().equals(supplierId)) {
                YunPianSmsAccount account = JSON.parseObject(accountEntity.getAccountConfig(), YunPianSmsAccount.class);
                pullYunPian(account);
            }
        }
    }

    /**
     * 构造smsClient
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


    private void pullTencent(TencentSmsAccount account) {
        try {
            //初始化客户端
            HttpProfile httpProfile = new HttpProfile();
            httpProfile.setEndpoint(account.getUrl());
            ClientProfile clientProfile = new ClientProfile();
            clientProfile.setHttpProfile(httpProfile);
            SmsClient smsClient = new SmsClient(new Credential(account.getSecretId(), account.getSecretKey()), account.getRegion(), clientProfile);
            //  每次拉取10条
            PullSmsSendStatusRequest req = new PullSmsSendStatusRequest();
            req.setLimit(10L);
            req.setSmsSdkAppId(account.getSmsSdkAppId());
            //  拉取回执后入库
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


    private void pullYunPian(YunPianSmsAccount account) {

    }

}
