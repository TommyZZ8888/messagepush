package com.zz.messagepush.handler.handler.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.zz.messagepush.common.domain.dto.SmsContentModel;
import com.zz.messagepush.common.domain.dto.SmsParam;
import com.zz.messagepush.common.domain.dto.TaskInfo;
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
public class SmsHandler implements Handler {

    @Autowired
    private SmsRecordMapper smsRecordMapper;

    @Autowired
    private SmsScript smsScript;

    @Override
    public boolean doHandler(TaskInfo taskInfo) {

        SmsContentModel smsContentModel = (SmsContentModel) taskInfo.getContentModel();
        String resultContent;
        if (StrUtil.isNotBlank(smsContentModel.getUrl())){
            resultContent = smsContentModel.getContent()+""+smsContentModel.getUrl();
        }else {
            resultContent = smsContentModel.getContent();
        }

        SmsParam build = SmsParam.builder()
                .phones(taskInfo.getReceiver())
                .content(resultContent)
                .messageTemplateId(taskInfo.getMessageTemplateId())
                .supplierId(MessageType.NOTICE.getCode())
                .supplierName(MessageType.NOTICE.getDescription()).build();
        List<SmsRecordEntity> recordEntityList = smsScript.send(build);

       if (CollUtil.isNotEmpty(recordEntityList)){
           smsRecordMapper.insertBatchSomeColumn(recordEntityList);
           return true;
       }
        return false;
    }
}
