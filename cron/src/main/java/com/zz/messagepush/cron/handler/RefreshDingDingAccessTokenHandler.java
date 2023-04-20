package com.zz.messagepush.cron.handler;

import com.dingtalk.api.DefaultDingTalkClient;
import com.dingtalk.api.DingTalkClient;
import com.dingtalk.api.request.OapiGettokenRequest;
import com.dingtalk.api.response.OapiGettokenResponse;
import com.taobao.api.ApiException;
import com.zz.messagepush.common.domain.dto.account.DingDingWorkNoticeAccount;
import com.zz.messagepush.support.config.SupportThreadPoolConfig;
import com.zz.messagepush.support.utils.AccountUtils;
import com.zz.messagepush.support.utils.RedisUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Description
 * @Author 张卫刚
 * @Date Created on 2023/4/18
 */

@Service
@Slf4j
public class RefreshDingDingAccessTokenHandler {


    private static final String DING_DING_ROBOT_ACCOUNT_KEY = "dingDingRobotAccount";

    private static final String PREFIX = "ding_ding_work_notice_";

    private static final String URL = "https://oapi.dingtalk.com/gettoken";


    @Autowired
    private RedisUtil redisUtil;

    @Autowired
    private AccountUtils accountUtils;

    public void execute() {
        SupportThreadPoolConfig.getPendingSingleThreadPool().execute(() -> {
            for (int i = 10; i < 1000; i = i + 10) {
                DingDingWorkNoticeAccount account = accountUtils.getAccount(10, DING_DING_ROBOT_ACCOUNT_KEY, PREFIX, DingDingWorkNoticeAccount.class);
                if (account == null) {
                    break;
                }
                String accessToken = getAccessToken(account);
            }
        });
    }

    private String getAccessToken(DingDingWorkNoticeAccount dingDingWorkNoticeAccount) {
        try {
            DingTalkClient client = new DefaultDingTalkClient(URL);
            OapiGettokenRequest oapiGettokenRequest = new OapiGettokenRequest();
            oapiGettokenRequest.setAppkey(dingDingWorkNoticeAccount.getAppKey());
            oapiGettokenRequest.setAppsecret(dingDingWorkNoticeAccount.getAppSecret());
            oapiGettokenRequest.setHttpMethod("GET");
            OapiGettokenResponse response = client.execute(oapiGettokenRequest);
            if (response.getErrcode() == 0) {
                return response.getAccessToken();
            }
        } catch (ApiException e) {
            e.printStackTrace();
        }
        return null;
    }

}
