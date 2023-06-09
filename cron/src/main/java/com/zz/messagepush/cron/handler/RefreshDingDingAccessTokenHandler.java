package com.zz.messagepush.cron.handler;

import com.alibaba.fastjson.JSON;
import com.dingtalk.api.DefaultDingTalkClient;
import com.dingtalk.api.DingTalkClient;
import com.dingtalk.api.request.OapiGettokenRequest;
import com.dingtalk.api.response.OapiGettokenResponse;
import com.taobao.api.ApiException;
import com.xxl.job.core.handler.annotation.XxlJob;
import com.zz.messagepush.common.constant.AustinConstant;
import com.zz.messagepush.common.constant.SendAccountConstant;
import com.zz.messagepush.common.domain.dto.account.DingDingWorkNoticeAccount;
import com.zz.messagepush.common.enums.ChannelType;
import com.zz.messagepush.support.config.SupportThreadPoolConfig;
import com.zz.messagepush.support.domain.entity.ChannelAccountEntity;
import com.zz.messagepush.support.mapper.ChannelAccountMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Description
 * @Author 张卫刚
 * @Date Created on 2023/4/18
 */

@Service
@Slf4j
public class RefreshDingDingAccessTokenHandler {

    private static final String URL = "https://oapi.dingtalk.com/gettoken";

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private ChannelAccountMapper channelAccountMapper;


    @XxlJob("refreshAccessTokenJob")
    public void execute() {
        SupportThreadPoolConfig.getPendingSingleThreadPool().execute(() -> {
            List<ChannelAccountEntity> accountList = channelAccountMapper.findAllByIsDeletedEqualsAndSendChannelEquals(AustinConstant.FALSE, ChannelType.DING_DING_WORK_NOTICE.getCode());
            for (ChannelAccountEntity channelAccountEntity : accountList) {
                DingDingWorkNoticeAccount account = JSON.parseObject(channelAccountEntity.getAccountConfig(), DingDingWorkNoticeAccount.class);
                String accessToken = getAccessToken(account);
                if (StringUtils.isNotBlank(accessToken)) {
                    redisTemplate.opsForValue().set(SendAccountConstant.DING_DING_ACCESS_TOKEN_PREFIX + channelAccountEntity.getId(), accessToken);
                }
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
