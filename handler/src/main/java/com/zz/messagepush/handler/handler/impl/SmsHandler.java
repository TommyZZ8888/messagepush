package com.zz.messagepush.handler.handler.impl;

import cn.hutool.core.collection.CollUtil;
import com.zz.messagepush.common.domain.dto.SmsParamDTO;
import com.zz.messagepush.common.domain.dto.TaskInfoDTO;
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
    public boolean doHandler(TaskInfoDTO taskInfoDTO) {
        SmsParamDTO build = SmsParamDTO.builder()
                .phones(taskInfoDTO.getReceiver())
                .content(taskInfoDTO.getContent())
                .messageTemplateId(taskInfoDTO.getMessageTemplateId())
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
