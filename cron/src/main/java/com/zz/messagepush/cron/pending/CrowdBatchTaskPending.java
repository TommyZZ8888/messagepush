package com.zz.messagepush.cron.pending;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.StrUtil;
import com.zz.messagepush.cron.config.CronAsyncThreadPoolConfig;
import com.zz.messagepush.cron.constant.PendingConstant;
import com.zz.messagepush.cron.domain.vo.CrowdInfoVO;
import com.zz.messagepush.service.api.domain.dto.BatchSendRequest;
import com.zz.messagepush.service.api.domain.dto.MessageParam;
import com.zz.messagepush.service.api.enums.BusinessCode;
import com.zz.messagepush.service.api.service.SendService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * @Description
 * @Author 张卫刚
 * @Date Created on 2023/4/6
 */
@Component
public class CrowdBatchTaskPending extends AbstractLazyPending<CrowdInfoVO> {

    @Autowired
    private SendService sendService;


    public CrowdBatchTaskPending() {
        PendingParam<CrowdInfoVO> pendingParam = new PendingParam<>();
        pendingParam.setThresholdNum(PendingConstant.NUM_THRESHOLD)
                .setBlockingQueue(new LinkedBlockingQueue(PendingConstant.QUEUE_SIZE))
                .setThresholdTime(PendingConstant.TIME_THRESHOLD)
                .setExecutorService(CronAsyncThreadPoolConfig.getConsumePendingThreadPool());
        this.pendingParam = pendingParam;
    }


    @Override
    public void doHandle(List<CrowdInfoVO> list) {
        //如果参数相同，组装成一个messageParam发送
        Map<Map<String, String>, String> paramsMap = MapUtil.newHashMap();
        for (CrowdInfoVO vo : list) {
            String receiver = vo.getReceiver();
            Map<String, String> params = vo.getParams();
            if (Objects.isNull(paramsMap.get(params))) {
                paramsMap.put(params, receiver);
            } else {
                String newReceiver = StringUtils.join(new String[]{paramsMap.get(params), receiver}, StrUtil.COMMA);
                paramsMap.put(params, newReceiver);
            }
        }

        //组装参数
        List<MessageParam> messageParamList = new ArrayList<>();
        for (Map.Entry<Map<String, String>, String> entry : paramsMap.entrySet()) {
            MessageParam messageParam = MessageParam.builder().receiver(entry.getValue()).variables(entry.getKey()).build();
            messageParamList.add(messageParam);
        }

        //发送
        BatchSendRequest batchSendRequest = BatchSendRequest.builder().code(BusinessCode.COMMON_SEND.getCode())
                .messageParamList(messageParamList)
                .messageTemplateId(CollUtil.getFirst(list.iterator()).getMessageTemplateId()).build();
        sendService.batchSend(batchSendRequest);
    }
}
