package com.zz.messagepush.handler.script.impl;

import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.IdUtil;
import com.tencentcloudapi.common.Credential;
import com.tencentcloudapi.common.profile.ClientProfile;
import com.tencentcloudapi.common.profile.HttpProfile;
import com.tencentcloudapi.sms.v20210111.SmsClient;
import com.tencentcloudapi.sms.v20210111.models.SendSmsRequest;
import com.tencentcloudapi.sms.v20210111.models.SendSmsResponse;
import com.tencentcloudapi.sms.v20210111.models.SendStatus;
import com.zz.messagepush.handler.domain.sms.SmsParam;
import com.zz.messagepush.common.domain.dto.account.sms.TencentSmsAccount;
import com.zz.messagepush.common.enums.SmsStatus;
import com.zz.messagepush.handler.script.SmsService;
import com.zz.messagepush.support.utils.AccountUtils;
import com.zz.messagepush.support.domain.entity.SmsRecordEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 * @Author 张卫刚
 * @Date Created on 2023/3/10
 * 1. 发送短信接入文档：<a href="https://cloud.tencent.com/document/api/382/55981">https://cloud.tencent.com/document/api/382/55981</a>
 * 2. 推荐直接使用SDK
 * 3. 推荐使用API Explorer 生成代码
 */

@Slf4j
@Component("TencentSmsService")
public class TencentSmsServiceImpl implements SmsService {

    private static final Integer PHONE_NUM = 11;


    @Autowired
    private AccountUtils accountUtils;


    @Override
    public List<SmsRecordEntity> send(SmsParam smsParam) {
        try {
            TencentSmsAccount tencentSmsParam = accountUtils.getAccountById(smsParam.getSendAccountId(), TencentSmsAccount.class);
            SmsClient client = init(tencentSmsParam);
            SendSmsRequest request = assembleReq(smsParam, tencentSmsParam);
            SendSmsResponse response = client.SendSms(request);
            return assembleSmsRecord(smsParam, response, tencentSmsParam);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public List<SmsRecordEntity> pull(String scriptName) {
        return null;
    }


    private List<SmsRecordEntity> assembleSmsRecord(SmsParam smsParamDTO, SendSmsResponse sendSmsResponse, TencentSmsAccount tencentSmsParam) {
        if (Objects.isNull(sendSmsResponse) || ArrayUtil.isEmpty(sendSmsResponse.getSendStatusSet())) {
            return null;
        }
        List<SmsRecordEntity> smsRecordList = new ArrayList<>();

        for (SendStatus sendStatus : sendSmsResponse.getSendStatusSet()) {
            String phone = new StringBuilder(new StringBuilder(sendStatus.getPhoneNumber())
                    .reverse().substring(0, PHONE_NUM)).reverse().toString();
            SmsRecordEntity build = SmsRecordEntity.builder()
                    .sendDate(new Date())
                    .messageTemplateId(smsParamDTO.getMessageTemplateId())
                    .phone(Long.valueOf(phone))
                    .supplierId(tencentSmsParam.getSupplierId())
                    .supplierName(tencentSmsParam.getSupplierName())
                    .seriesId(sendStatus.getSerialNo())
                    .chargingNum(Math.toIntExact(sendStatus.getFee()))
                    .status(SmsStatus.SEND_SUCCESS.getCode())
                    .msgContent(smsParamDTO.getContent())
                    .reportContent(sendStatus.getCode())
                    .created(new Date()).build();
            smsRecordList.add(build);
        }
        return smsRecordList;
    }

    /**
     * 组装参数
     * @param
     * @return
     */
    private SendSmsRequest assembleReq(SmsParam smsParam, TencentSmsAccount account) {
        SendSmsRequest smsRequest = new SendSmsRequest();
        smsRequest.setPhoneNumberSet(smsParam.getPhones().toArray(new String[smsParam.getPhones().size() - 1]));
        String[] templateParamSet1 = {smsParam.getContent()};
        smsRequest.setTemplateParamSet(templateParamSet1);
        smsRequest.setSmsSdkAppId(account.getSmsSdkAppId());
        smsRequest.setSignName(account.getSignName());
        smsRequest.setTemplateId(account.getTemplateId());
        smsRequest.setSessionContext(IdUtil.fastSimpleUUID());
        return smsRequest;
    }

    /**
     * 初始化client
     * @return
     */
    private SmsClient init(TencentSmsAccount account) {
        Credential cred = new Credential(account.getSecretId(), account.getSecretKey());
        HttpProfile httpProfile = new HttpProfile();
        httpProfile.setEndpoint(account.getUrl());
        ClientProfile clientProfile = new ClientProfile();
        clientProfile.setHttpProfile(httpProfile);
        return new SmsClient(cred, account.getUrl(), clientProfile);
    }

}
