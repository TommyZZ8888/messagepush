package com.zz.messagepush.handler.handler.impl;

import com.zz.messagepush.common.domain.dto.TaskInfo;
import com.zz.messagepush.common.domain.dto.account.DingDingWorkNoticeAccount;
import com.zz.messagepush.common.enums.ChannelType;
import com.zz.messagepush.handler.handler.BaseHandler;
import com.zz.messagepush.handler.handler.Handler;
import com.zz.messagepush.support.utils.AccountUtils;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @Description
 * @Author 张卫刚
 * @Date Created on 2023/4/18
 */
public class DingDingWorkNoticeHandler extends BaseHandler implements Handler {

    private final static String DING_DING_NOTICE_ACCOUNT_KEY = "dingDingNoticeAccount";
    private final static String PREFIX = "ding_ding_notice_account_";

    @Autowired
    private AccountUtils accountUtils;


    public DingDingWorkNoticeHandler() {
        channelCode = ChannelType.DING_DING_WORK_NOTICE.getCode();
    }

    @Override
    public boolean handler(TaskInfo taskInfo) {

        DingDingWorkNoticeAccount account = accountUtils.getAccount(10, DING_DING_NOTICE_ACCOUNT_KEY, PREFIX, new DingDingWorkNoticeAccount());
        return false;
    }
}
