package com.zz.messagepush.handler.handler.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.nacos.shaded.com.google.common.base.Throwables;
import com.zz.messagepush.common.domain.dto.SmsContentModel;
import com.zz.messagepush.common.domain.dto.SmsParam;
import com.zz.messagepush.common.domain.dto.TaskInfo;
import com.zz.messagepush.common.enums.ChannelType;
import com.zz.messagepush.handler.handler.BaseHandler;
import com.zz.messagepush.handler.handler.Handler;
import com.zz.messagepush.handler.script.SmsService;
import com.zz.messagepush.support.domain.entity.SmsRecordEntity;
import com.zz.messagepush.support.mapper.SmsRecordMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @Description
 * @Author 张卫刚
 * @Date Created on 2023/3/13
 */
@Component
@Slf4j
public class SmsHandler extends BaseHandler implements Handler {

    @Autowired
    private SmsRecordMapper smsRecordMapper;

    @Autowired
    private SmsService smsScript;


    public SmsHandler() {
        channelCode = ChannelType.SMS.getCode();
    }

    @Override
    public void doHandler(TaskInfo taskInfo)  {

        String resultContent = getSmsContent(taskInfo);

        SmsParam build = SmsParam.builder()
                .phones(taskInfo.getReceiver())
                .content(resultContent)
                .messageTemplateId(taskInfo.getMessageTemplateId())
                .sendAccount(taskInfo.getSendAccount())
                .build();
        try {
            List<SmsRecordEntity> recordEntityList = smsScript.send(build);

            if (CollUtil.isNotEmpty(recordEntityList)) {
                smsRecordMapper.saveAll(recordEntityList);
            }
        } catch (Exception e) {
            log.error("SmsHandler#handler fail:{},params:{}",
                    Throwables.getStackTraceAsString(e), JSON.toJSONString(build));
        }
    }

    @Override
    public boolean handler(TaskInfo taskInfo) {

        return false;
    }

    /**
     * 如果有输入链接，则把连接拼在文案后
     * ps：这里可以考虑将链接 转短链
     * ps：如果是营销类的短信，需考虑拼接 回TD退订 之类的文案
     *
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
