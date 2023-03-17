package com.zz.messagepush.handler.script;

import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.IdUtil;
import com.alibaba.fastjson.JSON;
import com.google.common.base.Throwables;
import com.tencentcloudapi.common.Credential;
import com.tencentcloudapi.common.exception.TencentCloudSDKException;
import com.tencentcloudapi.common.profile.ClientProfile;
import com.tencentcloudapi.common.profile.HttpProfile;
import com.tencentcloudapi.sms.v20210111.SmsClient;
import com.tencentcloudapi.sms.v20210111.models.SendSmsRequest;
import com.tencentcloudapi.sms.v20210111.models.SendSmsResponse;
import com.tencentcloudapi.sms.v20210111.models.SendStatus;
import com.zz.messagepush.common.domain.dto.SmsParam;
import com.zz.messagepush.common.enums.SmsStatus;
import com.zz.messagepush.support.domain.entity.SmsRecordEntity;
import com.zz.messagepush.support.utils.OkHttpUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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
public class TencentSmsScript implements SmsScript {

    @Autowired
    private OkHttpUtils okHttpUtils;

    private static final Integer PHONE_NUM = 11;

    /**
     * api 相关
     */
    private static final String URL = "sms.tencentcloudapi.com";
    private static final String REGION = "ap-nanjing";

    /**
     * 账号相关
     */

    @Value("${tencent.sms.account.secret-id}")
    private String SECRET_ID;
    @Value("${tencent.sms.account.secret-key}")
    private String SECRET_KEY;
    @Value("${tencent.sms.account.sms-sdk-app-id}")
    private String SMS_SDK_APP_ID;
    @Value("${tencent.sms.account.template-id}")
    private String TEMPLATE_ID;
    @Value("${tencent.sms.account.sign-name}")
    private String SIGN_NAME;


    @Override
    public List<SmsRecordEntity> send(SmsParam smsParam) {

        try {
            SmsClient client = init();
            SendSmsRequest sendSmsRequest = assembleReq(smsParam);

            SendSmsResponse response = client.SendSms(sendSmsRequest);

            return assembleSmsRecord(smsParam, response);
        } catch (TencentCloudSDKException e) {
            log.error("send tencent sms fail!{},params:{}", Throwables.getStackTraceAsString(e), JSON.toJSONString(smsParam));
            return null;
        }
    }


    private List<SmsRecordEntity> assembleSmsRecord(SmsParam smsParamDTO, SendSmsResponse sendSmsResponse) {
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
                    .supplierId(smsParamDTO.getSupplierId())
                    .supplierName(smsParamDTO.getSupplierName())
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
     * @param smsParamDTO
     * @return
     */
    private SendSmsRequest assembleReq(SmsParam smsParamDTO) {
        SendSmsRequest smsRequest = new SendSmsRequest();
        smsRequest.setPhoneNumberSet(smsParamDTO.getPhones().toArray(new String[smsParamDTO.getPhones().size() - 1]));
        String[] templateParamSet1 = {smsParamDTO.getContent()};
        smsRequest.setTemplateParamSet(templateParamSet1);
        smsRequest.setSmsSdkAppId(SMS_SDK_APP_ID);
        smsRequest.setTemplateId(TEMPLATE_ID);
        smsRequest.setSignName(SIGN_NAME);
        smsRequest.setSessionContext(IdUtil.fastSimpleUUID());
        return smsRequest;
    }

    /**
     * 初始化client
     *
     * @return
     */
    private SmsClient init() {
        Credential credential = new Credential(SECRET_ID, SECRET_KEY);
        HttpProfile httpProfile = new HttpProfile();
        httpProfile.setEndpoint(URL);
        ClientProfile clientProfile = new ClientProfile();
        clientProfile.setHttpProfile(httpProfile);
        return new SmsClient(credential, REGION, clientProfile);
    }
}
