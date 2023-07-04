package com.zz.messagepush.handler.receipt.starter.impl;

import cn.hutool.core.collection.CollUtil;
import com.alibaba.fastjson.JSON;
import com.zz.messagepush.common.constant.CommonConstant;
import com.zz.messagepush.common.domain.dto.account.sms.SmsAccount;
import com.zz.messagepush.common.enums.ChannelType;
import com.zz.messagepush.handler.receipt.starter.ReceiptMessageStarter;
import com.zz.messagepush.handler.script.SmsService;
import com.zz.messagepush.support.domain.entity.ChannelAccountEntity;
import com.zz.messagepush.support.domain.entity.SmsRecordEntity;
import com.zz.messagepush.support.mapper.ChannelAccountMapper;
import com.zz.messagepush.support.mapper.SmsRecordMapper;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Map;

/**
 * @Description 拉取短息回执信息
 * @Author 张卫刚
 * @Date Created on 2023/6/14
 */
public class SmsPullReceiptStarterImpl implements ReceiptMessageStarter {

    @Autowired
    private Map<String, SmsService> smsServiceMap;

    @Autowired
    private SmsRecordMapper smsRecordMapper;

    @Autowired
    private ChannelAccountMapper channelAccountMapper;


    /**
     * 拉取消息并入库
     */
    @Override
    public void start() {
        List<ChannelAccountEntity> accountEntityList = channelAccountMapper.findAllByIsDeletedEqualsAndSendChannelEquals(CommonConstant.FALSE, ChannelType.SMS.getCode());
        for (ChannelAccountEntity channelAccountEntity : accountEntityList) {
            SmsAccount smsAccount = JSON.parseObject(channelAccountEntity.getAccountConfig(), SmsAccount.class);
            List<SmsRecordEntity> recordEntities = smsServiceMap.get(smsAccount.getScriptName()).pull(smsAccount.getSupplierId());
            if (CollUtil.isNotEmpty(recordEntities)) {
                smsRecordMapper.saveAll(recordEntities);
            }
        }
    }
}
