package com.zz.messagepush.handler.script.impl;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.crypto.SecureUtil;
import cn.hutool.http.Header;
import cn.hutool.http.HttpException;
import cn.hutool.http.HttpRequest;
import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson.JSON;
import com.google.common.base.Throwables;
import com.zz.messagepush.common.domain.dto.account.sms.LinTongSmsAccount;
import com.zz.messagepush.common.enums.SmsStatus;
import com.zz.messagepush.handler.domain.sms.LinTongSendMessage;
import com.zz.messagepush.handler.domain.sms.LinTongSendResult;
import com.zz.messagepush.handler.domain.sms.SmsParam;
import com.zz.messagepush.handler.script.SmsService;
import com.zz.messagepush.support.domain.entity.SmsRecordEntity;
import com.zz.messagepush.support.utils.AccountUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * @Description LinTongSmsServiceImpl
 * @Author 张卫刚
 * @Date Created on 2023/7/4
 */
@Slf4j
@Component("LinTongSmsService")
public class LinTongSmsServiceImpl implements SmsService {

    @Autowired
    private AccountUtils accountUtils;

    @Override
    public List<SmsRecordEntity> send(SmsParam smsParamDTO) {
        try {
            LinTongSmsAccount account = accountUtils.getSmsAccountByScriptName(smsParamDTO.getScriptName(), LinTongSmsAccount.class);
            String req = assembleReq(smsParamDTO, account);
            String response = HttpRequest.post(account.getUrl()).body(req)
                    .header(Header.ACCEPT.getValue(), "application/json")
                    .header(Header.CONTENT_TYPE.getValue(), "application/json;charset=utf-8")
                    .timeout(2000)
                    .execute().body();
            LinTongSendResult linTongSendResult = JSON.parseObject(response, LinTongSendResult.class);
            return assembleSmsRecord(smsParamDTO, linTongSendResult, account);
        } catch (HttpException e) {
            log.error("LinTongSmsServiceImpl#send fail:{},param:{}", Throwables.getStackTraceAsString(e), JSON.toJSONString(smsParamDTO));
            return new ArrayList<>();
        }
    }

    @Override
    public List<SmsRecordEntity> pull(Integer id) {
        return null;
    }

    private String assembleReq(SmsParam smsParam, LinTongSmsAccount account) {
        Map<String, Object> map = new HashMap<>(5);
        final long time = DateUtil.date().getTime();
        String sign = SecureUtil.md5(account.getUserName() + time + SecureUtil.md5(account.getPassword()));
        map.put("userName", account.getUserName());
        //获取当前时间戳
        map.put("timestamp", time);
        List<LinTongSendMessage> linTongSendMessages = new ArrayList<>(smsParam.getPhones().size());
        for (String phone : smsParam.getPhones()) {
            linTongSendMessages.add(LinTongSendMessage.builder().phone(phone).content(smsParam.getContent()).build());
        }
        map.put("messageList", linTongSendMessages);
        map.put("sign", sign);
        return JSONUtil.toJsonStr(map);
    }


    private List<SmsRecordEntity> assembleSmsRecord(SmsParam smsParam, LinTongSendResult sendResponse, LinTongSmsAccount account) {
        if (sendResponse == null || ArrayUtil.isEmpty(sendResponse.getDataDTOS())) {
            return null;
        }

        List<SmsRecordEntity> smsRecordList = new ArrayList<>();

        for (LinTongSendResult.DataDTO datum : sendResponse.getDataDTOS()) {
            SmsRecordEntity smsRecord = SmsRecordEntity.builder()
                    .sendDate(new Date())
                    .messageTemplateId(smsParam.getMessageTemplateId())
                    .phone(Long.valueOf(datum.getPhone()))
                    .supplierId(account.getSupplierId())
                    .supplierName(account.getSupplierName())
                    .msgContent(smsParam.getContent())
                    .seriesId(datum.getMsgId().toString())
                    .chargingNum(1)
                    .status(datum.getCode() == 0 ? SmsStatus.SEND_SUCCESS.getCode() : SmsStatus.SEND_FAIL.getCode())
                    .reportContent(datum.getMessage())
                    .created(new Date())
                    .updated(new Date())
                    .build();

            smsRecordList.add(smsRecord);
        }
        return smsRecordList;
    }
}
