package com.zz.messagepush.handler.receipt.starter.impl;

import com.zz.messagepush.common.constant.CommonConstant;
import com.zz.messagepush.common.enums.ChannelType;
import com.zz.messagepush.handler.handler.impl.DingDingWorkNoticeHandler;
import com.zz.messagepush.handler.receipt.starter.ReceiptMessageStarter;
import com.zz.messagepush.support.domain.entity.ChannelAccountEntity;
import com.zz.messagepush.support.mapper.ChannelAccountMapper;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * @Description 拉取钉钉工作消息的回执 
 * @Author 张卫刚
 * @Date Created on 2023/6/14
 */
public class DingDingWorkReceiptStarterImpl implements ReceiptMessageStarter {

    @Autowired
    private DingDingWorkNoticeHandler dingDingWorkNoticeHandler;

    @Autowired
    private ChannelAccountMapper channelAccountMapper;


    @Override
    public void start() {
        List<ChannelAccountEntity> accountEntityList = channelAccountMapper.findAllByIsDeletedEqualsAndSendChannelEquals(CommonConstant.FALSE, ChannelType.DING_DING_WORK_NOTICE.getCode());
        for (ChannelAccountEntity channelAccountEntity : accountEntityList) {
            dingDingWorkNoticeHandler.pull(channelAccountEntity.getId());
        }
    }
}
