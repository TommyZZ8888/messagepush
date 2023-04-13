package com.zz.messagepush.cron.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.text.csv.CsvRow;
import cn.hutool.core.util.StrUtil;
import com.zz.messagepush.common.utils.ApplicationContextUtil;
import com.zz.messagepush.cron.constant.PendingConstant;
import com.zz.messagepush.cron.csv.CountFileRowHandler;
import com.zz.messagepush.cron.domain.vo.CrowdInfoVO;
import com.zz.messagepush.cron.pending.AbstractLazyPending;
import com.zz.messagepush.cron.pending.CrowdBatchTaskPending;
import com.zz.messagepush.cron.pending.PendingParam;
import com.zz.messagepush.cron.service.TaskHandler;
import com.zz.messagepush.cron.utils.ReadFileUtils;
import com.zz.messagepush.support.domain.entity.MessageTemplateEntity;
import com.zz.messagepush.support.mapper.MessageTemplateMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * @Description
 * @Author 张卫刚
 * @Date Created on 2023/4/4
 */

@Service
@Slf4j
public class TaskHandlerImpl implements TaskHandler {

    @Autowired
    private MessageTemplateMapper messageTemplateMapper;


    @Override
    public void handler(Long messageTemplateId) {
        MessageTemplateEntity messageTemplateEntity = messageTemplateMapper.findById(messageTemplateId).orElse(null);

        assert messageTemplateEntity != null;
        Long countCsvRow = ReadFileUtils.countCsvRow(messageTemplateEntity.getCronCrowdPath(), new CountFileRowHandler());

        if (messageTemplateEntity.getCronCrowdPath() == null) {
            return;
        }

        CrowdBatchTaskPending crowdBatchTaskPending = ApplicationContextUtil.getBean(CrowdBatchTaskPending.class);

        // 读取文件得到每一行记录给到队列做batch处理
        ReadFileUtils.getCsvRow(messageTemplateEntity.getCronCrowdPath(), row -> {
            if (CollUtil.isEmpty(row.getFieldMap())
                    || StrUtil.isBlank(row.getFieldMap().get(ReadFileUtils.RECEIVER_KEY))) {
                return;
            }
            Map<String, String> params = ReadFileUtils.getParamFromLine(row.getFieldMap());
            CrowdInfoVO crowdInfoVo = CrowdInfoVO.builder().receiver(row.getFieldMap().get(ReadFileUtils.RECEIVER_KEY))
                    .params(params)
                    .messageTemplateId(messageTemplateId).build();
            crowdBatchTaskPending.pending(crowdInfoVo);

            onComplete(row,countCsvRow,crowdBatchTaskPending,messageTemplateId);
        });

    }


    /**
     * 文章暂停结束时
     * 暂停单线程消费
     * 更改消息模板的状态
     * @param csvRow
     * @param countCsvRow
     * @param abstractLazyPending
     * @param messageTemplateId
     */
    public void onComplete(CsvRow csvRow, Long countCsvRow, AbstractLazyPending abstractLazyPending, Long messageTemplateId) {
        if (csvRow.getFieldCount() == countCsvRow) {
            abstractLazyPending.setStop(true);
            log.info("messageTemplate:[{}] read csv file complete!", messageTemplateId);
        }
    }
}
