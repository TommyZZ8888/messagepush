package com.zz.messagepush.cron.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.zz.messagepush.cron.domain.vo.CrowdInfoVO;
import com.zz.messagepush.cron.service.TaskHandler;
import com.zz.messagepush.cron.utils.ReadFileUtils;
import com.zz.messagepush.support.domain.entity.MessageTemplateEntity;
import com.zz.messagepush.support.mapper.MessageTemplateMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Description
 * @Author 张卫刚
 * @Date Created on 2023/4/4
 */

@Service
public class TaskHandlerImpl implements TaskHandler {

    @Autowired
    private MessageTemplateMapper messageTemplateMapper;

    @Override
    public void handler(Long messageTemplateId) {
        MessageTemplateEntity messageTemplateEntity = messageTemplateMapper.findById(messageTemplateId).orElse(null);

        if (messageTemplateEntity == null || messageTemplateEntity.getCronCrowdPath() == null) {
            return;
        }

        List<CrowdInfoVO> csvRowList = ReadFileUtils.getCsvRowList(messageTemplateEntity.getCronCrowdPath());

        if (CollUtil.isEmpty(csvRowList)) {

        }

    }
}
