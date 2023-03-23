package com.zz.messagepush.handler.handler.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.zz.messagepush.common.domain.dto.SmsContentModel;
import com.zz.messagepush.common.domain.dto.SmsParam;
import com.zz.messagepush.common.domain.dto.TaskInfo;
import com.zz.messagepush.common.enums.ChannelType;
import com.zz.messagepush.common.enums.MessageType;
import com.zz.messagepush.handler.handler.Handler;
import com.zz.messagepush.handler.script.SmsScript;
import com.zz.messagepush.support.domain.entity.SmsRecordEntity;
import com.zz.messagepush.support.mapper.SmsRecordMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @Description
 * @Author 张卫刚
 * @Date Created on 2023/3/13
 */
@Component
public class SmsHandler extends Handler {

    @Autowired
    private SmsRecordMapper smsRecordMapper;

    @Autowired
    private SmsScript smsScript;


    public SmsHandler(){
        channelCode = ChannelType.SMS.getCode();
    }

    @Override
    public void doHandler(TaskInfo taskInfo) {

        String resultContent = getSmsContent(taskInfo);

        SmsParam build = SmsParam.builder()
                .phones(taskInfo.getReceiver())
                .content(resultContent)
                .messageTemplateId(taskInfo.getMessageTemplateId())
                .supplierId(MessageType.NOTICE.getCode())
                .supplierName(MessageType.NOTICE.getDescription()).build();
        List<SmsRecordEntity> recordEntityList = smsScript.send(build);

        if (CollUtil.isNotEmpty(recordEntityList)) {
            smsRecordMapper.insertBatchSomeColumn(recordEntityList);
        }
    }

    @Override
    public void handler(TaskInfo taskInfo) {

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
}
