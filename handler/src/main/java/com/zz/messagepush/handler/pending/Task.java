package com.zz.messagepush.handler.pending;

import cn.hutool.core.collection.CollUtil;
import com.zz.messagepush.common.domain.dto.TaskInfo;
import com.zz.messagepush.handler.deduplication.DeduplicationRuleService;
import com.zz.messagepush.handler.discard.DiscardMessageService;
import com.zz.messagepush.handler.handler.HandlerHolder;
import com.zz.messagepush.handler.shield.ShieldService;
import lombok.Data;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @Description task执行器，1.通用去重  2.发送消息
 * @Author 张卫刚
 * @Date Created on 2023/3/22
 */
@Data
@Accessors(chain = true)
@Slf4j
public class Task implements Runnable {

    @Autowired
    private HandlerHolder handlerHolder;

    @Autowired
    private DiscardMessageService discardMessageService;

    @Autowired
    private DeduplicationRuleService deduplicationRuleService;

    private TaskInfo taskInfo;

    @Autowired
    private ShieldService shieldService;


    @Override
    public void run() {
        //TODO 丢弃信息
        if (discardMessageService.isDiscard(taskInfo)) {
            return;
        }

        shieldService.shield(taskInfo);

        //TODO 通用去重
        if (CollUtil.isNotEmpty(taskInfo.getReceiver())) {
            deduplicationRuleService.duplication(taskInfo);
            //发送消息
            handlerHolder.route(taskInfo.getSendChannel()).doHandler(taskInfo);
        }
    }
}
