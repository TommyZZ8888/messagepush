package com.zz.messagepush.handler.script;

import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.IdUtil;
import com.tencentcloudapi.common.Credential;
import com.tencentcloudapi.common.profile.ClientProfile;
import com.tencentcloudapi.common.profile.HttpProfile;
import com.tencentcloudapi.sms.v20210111.SmsClient;
import com.tencentcloudapi.sms.v20210111.models.SendSmsRequest;
import com.tencentcloudapi.sms.v20210111.models.SendSmsResponse;
import com.tencentcloudapi.sms.v20210111.models.SendStatus;
import com.zz.messagepush.common.domain.dto.SmsParam;
import com.zz.messagepush.common.enums.SmsStatus;
import com.zz.messagepush.common.domain.dto.account.TencentSmsParam;
import com.zz.messagepush.support.utils.AccountUtils;
import com.zz.messagepush.support.domain.entity.SmsRecordEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @Author 张卫刚
 * @Date Created on 2023/3/10
 * 1. 发送短信接入文档：<a href="https://cloud.tencent.com/document/api/382/55981">https://cloud.tencent.com/document/api/382/55981</a>
 * 2. 推荐直接使用SDK
 * 3. 推荐使用API Explorer 生成代码
 */
@Service
@Slf4j
public class TencentSmsServiceImpl implements SmsService {

    private static final Integer PHONE_NUM = 11;
    private static final String SMS_ACCOUNT_KEY = "smsAccount";
    private static final String PREFIX = "sms_";


    @Autowired
    private AccountUtils accountUtils;


    @Override
    public List<SmsRecordEntity> send(SmsParam smsParam) throws Exception {
        TencentSmsParam tencentSmsParam = accountUtils.getAccount(smsParam.getSendAccount(), SMS_ACCOUNT_KEY, PREFIX, TencentSmsParam.class);
        SmsClient client = init(tencentSmsParam);
        SendSmsRequest request = assembleReq(smsParam, tencentSmsParam);
        SendSmsResponse response = client.SendSms(request);
        return assembleSmsRecord(smsParam, response, tencentSmsParam);
    }


    private List<SmsRecordEntity> assembleSmsRecord(SmsParam smsParamDTO, SendSmsResponse sendSmsResponse, TencentSmsParam tencentSmsParam) {
        if (sendSmsResponse == null || ArrayUtil.isEmpty(sendSmsResponse.getSendStatusSet())) {
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
     *
     * @param
     * @return
     */
    private SendSmsRequest assembleReq(SmsParam smsParam, TencentSmsParam account) {
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
     *
     * @return
     */
    private SmsClient init(TencentSmsParam account) {
        Credential cred = new Credential(account.getSecretId(), account.getSecretKey());
        HttpProfile httpProfile = new HttpProfile();
        httpProfile.setEndpoint(account.getUrl());
        ClientProfile clientProfile = new ClientProfile();
        clientProfile.setHttpProfile(httpProfile);
        return new SmsClient(cred, account.getUrl(), clientProfile);
    }

}
