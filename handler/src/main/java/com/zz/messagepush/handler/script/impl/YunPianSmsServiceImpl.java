package com.zz.messagepush.handler.script.impl;

import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.ContentType;
import cn.hutool.http.Header;
import cn.hutool.http.HttpRequest;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.zz.messagepush.common.constant.SendAccountConstant;
import com.zz.messagepush.handler.domain.sms.SmsParam;
import com.zz.messagepush.common.domain.dto.account.YunPianSmsAccount;
import com.zz.messagepush.common.enums.SmsStatus;
import com.zz.messagepush.handler.domain.sms.YunPianSendResult;
import com.zz.messagepush.handler.script.BaseSmsScript;
import com.zz.messagepush.handler.script.SmsService;
import com.zz.messagepush.handler.script.anno.SmsScriptHandler;
import com.zz.messagepush.support.domain.entity.SmsRecordEntity;
import com.zz.messagepush.support.utils.AccountUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.*;

/**
 * @Description YunPianSmsServiceImpl
 * @Author 张卫刚
 * @Date Created on 2023/5/4
 */
@SmsScriptHandler(value = "YunPianSmsService")
public class YunPianSmsServiceImpl extends BaseSmsScript implements SmsService {

    @Autowired
    private AccountUtils accountUtils;

    @Override
    public List<SmsRecordEntity> send(SmsParam smsParam)     {
        YunPianSmsAccount account = accountUtils.getAccount(SendAccountConstant.YUN_PIAN_SMS_CODE, SendAccountConstant.SMS_ACCOUNT_KEY, SendAccountConstant.SMS_ACCOUNT_PREFIX, YunPianSmsAccount.class);

        Map<String, String> params = assembleReq(smsParam, account);
        String result = HttpRequest.post(account.getUrl())
                .header(Header.CONTENT_TYPE.getValue(), "application/x-www-form-urlencoded;charset=utf-8;")
                .header(Header.ACCEPT.getValue(), "application/json;charset=utf-8;")
                .body(JSON.toJSONString(params))
                .timeout(2000)
                .execute().body();

        YunPianSendResult yunPianSendResult = JSONObject.parseObject(result, YunPianSendResult.class);
        return assembleSmsRecord(smsParam, yunPianSendResult, account);
    }


    private List<SmsRecordEntity> assembleSmsRecord(SmsParam smsParamDTO, YunPianSendResult yunPianSendResult, YunPianSmsAccount account) {
        if (yunPianSendResult == null || ArrayUtil.isEmpty(yunPianSendResult.getData())) {
            return null;
        }
        List<SmsRecordEntity> smsRecordList = new ArrayList<>();

        for (YunPianSendResult.DataDTO datum : yunPianSendResult.getData()) {
            SmsRecordEntity build = SmsRecordEntity.builder()
                    .sendDate(new Date())
                    .messageTemplateId(smsParamDTO.getMessageTemplateId())
                    .phone(Long.valueOf(datum.getMobile()))
                    .supplierId(Integer.valueOf(account.getSupplierId()))
                    .supplierName(account.getSupplierName())
                    .seriesId(datum.getSid())
                    .chargingNum(Math.toIntExact(datum.getCount()))
                    .status(datum.getCode().equals(0) ? SmsStatus.SEND_SUCCESS.getCode() : SmsStatus.SEND_FAIL.getCode())
                    .msgContent(smsParamDTO.getContent())
                    .reportContent(datum.getMsg())
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
    private Map<String, String> assembleReq(SmsParam smsParam, YunPianSmsAccount account) {
        Map<String, String> params = new HashMap<>(8);
        params.put("apikey", account.getApikey());
        params.put("mobile", StringUtils.join(smsParam.getPhones(), StrUtil.C_COMMA));
        params.put("tpl_id", account.getTplId());
        params.put("tpl_value", "");
        return params;
    }
}