package com.zz.messagepush.handler.handler.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.nacos.shaded.com.google.common.base.Throwables;
import com.dingtalk.api.DefaultDingTalkClient;
import com.dingtalk.api.request.OapiGettokenRequest;
import com.dingtalk.api.response.OapiGettokenResponse;
import com.taobao.api.ApiException;
import com.zz.messagepush.common.constant.CommonConstant;
import com.zz.messagepush.common.constant.SendAccountConstant;
import com.zz.messagepush.common.domain.dto.account.DingDingWorkNoticeAccount;
import com.zz.messagepush.common.enums.ChannelType;
import com.zz.messagepush.support.config.SupportThreadPoolConfig;
import com.zz.messagepush.support.domain.entity.ChannelAccountEntity;
import com.zz.messagepush.support.mapper.ChannelAccountMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @Description RefreshDingDIngAccessTokenHandler
 * @Author 张卫刚
 * @Date Created on 2023/4/26
 */
@Slf4j
@Component
public class RefreshDingDingAccessTokenHandler {


    private static final String URL = "https://oapi.dingtalk.com/gettoken";

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Autowired
    private ChannelAccountMapper channelAccountMapper;


    public void execute() {
        SupportThreadPoolConfig.getPendingSingleThreadPool().execute(() -> {
            List<ChannelAccountEntity> list = channelAccountMapper.findAllByIsDeletedEqualsAndSendChannelEquals(CommonConstant.FALSE, ChannelType.DING_DING_WORK_NOTICE.getCode());
            for (ChannelAccountEntity channelAccountEntity : list) {
                DingDingWorkNoticeAccount account = JSON.parseObject(channelAccountEntity.getAccountConfig(), DingDingWorkNoticeAccount.class);
                if (account == null) {
                    return;
                }
                String accessToken = getAccessToken(account);
                if (StringUtils.isNotBlank(accessToken)) {
                    redisTemplate.opsForValue().set(SendAccountConstant.DING_DING_ACCESS_TOKEN_PREFIX + channelAccountEntity.getId(), accessToken);
                }
            }
        });
    }

    public String getAccessToken(DingDingWorkNoticeAccount account) {
        String accessToken = "";
        try {
            DefaultDingTalkClient client = new DefaultDingTalkClient(URL);
            OapiGettokenRequest request = new OapiGettokenRequest();
            request.setAppsecret(account.getAppKey());
            request.setAppsecret(account.getAppSecret());
            request.setHttpMethod(CommonConstant.REQUEST_METHOD_GET);
            OapiGettokenResponse execute = client.execute(request);
            accessToken = execute.getAccessToken();
        } catch (ApiException e) {
            log.error("refreshDingDingAccessToken#getAccessToken fail : {}", Throwables.getStackTraceAsString(e));
        }
        return accessToken;
    }
}
