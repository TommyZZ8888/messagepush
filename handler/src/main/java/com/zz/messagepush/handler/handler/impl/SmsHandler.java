package com.zz.messagepush.handler.handler.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.ctrip.framework.apollo.Config;
import com.ctrip.framework.apollo.spring.annotation.ApolloConfig;
import com.zz.messagepush.common.constant.AustinConstant;
import com.zz.messagepush.common.constant.CommonConstant;
import com.zz.messagepush.common.domain.dto.account.sms.SmsAccount;
import com.zz.messagepush.common.domain.dto.model.SmsContentModel;
import com.zz.messagepush.handler.domain.sms.MessageTypeSmsConfig;
import com.zz.messagepush.handler.domain.sms.SmsParam;
import com.zz.messagepush.common.domain.dto.TaskInfo;
import com.zz.messagepush.common.enums.ChannelType;
import com.zz.messagepush.handler.handler.BaseHandler;
import com.zz.messagepush.handler.handler.Handler;
import com.zz.messagepush.handler.script.SmsService;
import com.zz.messagepush.support.domain.entity.MessageTemplateEntity;
import com.zz.messagepush.support.domain.entity.SmsRecordEntity;
import com.zz.messagepush.support.mapper.SmsRecordMapper;
import com.zz.messagepush.support.utils.AccountUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * @Description
 * @Author 张卫刚
 * @Date Created on 2023/3/13
 */
@Component
@Slf4j
public class SmsHandler extends BaseHandler implements Handler {

    @Autowired
    private AccountUtils accountUtils;

    @Autowired
    private SmsRecordMapper smsRecordMapper;

    @Autowired
    private Map<String, SmsService> smsServiceMap;

    @ApolloConfig("boss.austin")
    private Config config;

    private static final String FLOW_KEY = "msgTypeSmsConfig";
    private static final String FLOW_KEY_PREFIX = "message_type_";
    private static final Integer AUTO_FLOW_TRUE = 0;

    public SmsHandler() {
        channelCode = ChannelType.SMS.getCode();
    }


    @Override
    public boolean handler(TaskInfo taskInfo) {
        String resultContent = getSmsContent(taskInfo);

        SmsParam smsParam = SmsParam.builder()
                .phones(taskInfo.getReceiver())
                .content(resultContent)
                .messageTemplateId(taskInfo.getMessageTemplateId())
                .sendAccountId(taskInfo.getSendAccount())
                .build();
        try {
            MessageTypeSmsConfig[] messageTypeSmsConfigs = loadBalance(Objects.requireNonNull(messageTypeSmsConfigs(taskInfo)));
            assert messageTypeSmsConfigs != null;
            for (MessageTypeSmsConfig messageTypeSmsConfig : messageTypeSmsConfigs) {
                smsParam.setScriptName(messageTypeSmsConfig.getScriptName());
                smsParam.setSendAccountId(messageTypeSmsConfig.getSendAccount());
                List<SmsRecordEntity> list = smsServiceMap.get(messageTypeSmsConfig.getScriptName()).send(smsParam);
                if (CollUtil.isNotEmpty(list)) {
                    smsRecordMapper.saveAll(list);
                    return true;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 如果有输入链接，则把连接拼在文案后
     * ps：这里可以考虑将链接 转短链
     * ps：如果是营销类的短信，需考虑拼接 回TD退订 之类的文案
     * @param taskInfo
     * @return
     */
    private String getSmsContent(TaskInfo taskInfo) {
        SmsContentModel smsContentModel = (SmsContentModel) taskInfo.getContentModel();
        if (StrUtil.isNotBlank(smsContentModel.getUrl())) {
            return smsContentModel.getContent() + "" + smsContentModel.getUrl();
        } else {
            return smsContentModel.getContent();
        }
    }


    /**
     * 流量负载
     * 根据配置的权重优先走某个账号，并取出一个备份的
     * @param messageTypeSmsConfigs
     * @return
     */
    private MessageTypeSmsConfig[] loadBalance(List<MessageTypeSmsConfig> messageTypeSmsConfigs) {
        int total;
        total = messageTypeSmsConfigs.stream().mapToInt(MessageTypeSmsConfig::getWeights).sum();

        Random random = new Random();
        int index = random.nextInt(total) + 1;

        MessageTypeSmsConfig supplier;
        MessageTypeSmsConfig supplierBack;
        for (int i = 0; i < messageTypeSmsConfigs.size(); i++) {
            if (index <= messageTypeSmsConfigs.get(i).getWeights()) {
                supplier = messageTypeSmsConfigs.get(i);

                //取出下一个供应商
                int j = (i + 1) % messageTypeSmsConfigs.size();
                if (i == j) {
                    return new MessageTypeSmsConfig[]{supplier};
                }
                supplierBack = messageTypeSmsConfigs.get(j);
                return new MessageTypeSmsConfig[]{supplier, supplierBack};
            }
            index -= messageTypeSmsConfigs.get(i).getWeights();
        }
        return null;
    }


    /**
     * 每种类型都会有其下发渠道账号的配置(流量占比也会配置里面)
     * <p>
     * 样例：
     * key：msg_type_sms_config
     * value：[{"message_type_10":[{"weights":80,"scriptName":"TencentSmsScript"},{"weights":20,"scriptName":"YunPianSmsScript"}]},{"message_type_20":[{"weights":20,"scriptName":"YunPianSmsScript"}]},{"message_type_30":[{"weights":20,"scriptName":"TencentSmsScript"}]},{"message_type_40":[{"weights":20,"scriptName":"TencentSmsScript"}]}]
     * 通知类短信有两个发送渠道 TencentSmsScript 占80%流量，YunPianSmsScript占20%流量
     * 营销类短信只有一个发送渠道 YunPianSmsScript
     * 验证码短信只有一个发送渠道 TencentSmsScript
     * @param
     * @return
     */
    private List<MessageTypeSmsConfig> messageTypeSmsConfigs(TaskInfo taskInfo) {
        if (!AUTO_FLOW_TRUE.equals(taskInfo.getSendAccount())) {
            SmsAccount account = accountUtils.getAccountById(taskInfo.getSendAccount(), SmsAccount.class);
            return Arrays.asList(MessageTypeSmsConfig.builder().sendAccount(taskInfo.getSendAccount()).scriptName(account.getScriptName()).weights(100).build());
        }
        String property = config.getProperty(FLOW_KEY, CommonConstant.EMPTY_VALUE_JSON_ARRAY);
        JSONArray jsonArray = JSON.parseArray(property);
        for (int i = 0; i < jsonArray.size(); i++) {
            JSONArray array = jsonArray.getJSONObject(i).getJSONArray(FLOW_KEY_PREFIX + taskInfo.getMsgType());
            if (CollUtil.isNotEmpty(array)) {
                return JSON.parseArray(JSON.toJSONString(array), MessageTypeSmsConfig.class);
            }
        }
        return null;
    }

    @Override
    public void recall(MessageTemplateEntity messageTemplate) {

    }
}
