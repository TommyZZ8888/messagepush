package com.zz.messagepush.handler.handler.impl;

import cn.hutool.core.util.StrUtil;
import cn.hutool.http.ContentType;
import cn.hutool.http.Header;
import cn.hutool.http.HttpException;
import cn.hutool.http.HttpRequest;
import com.alibaba.fastjson.JSON;
import com.alibaba.nacos.shaded.com.google.common.base.Throwables;
import com.xxl.job.core.handler.annotation.XxlJob;
import com.zz.messagepush.common.constant.CommonConstant;
import com.zz.messagepush.common.constant.SendAccountConstant;
import com.zz.messagepush.common.domain.dto.account.GeTuiAccount;
import cn.hutool.crypto.SecureUtil;
import com.zz.messagepush.common.enums.ChannelType;
import com.zz.messagepush.cron.domain.dto.geTui.GeTuiTokenResultDTO;
import com.zz.messagepush.cron.domain.dto.geTui.QueryTokenParamDTO;
import com.zz.messagepush.support.config.SupportThreadPoolConfig;
import com.zz.messagepush.support.domain.entity.ChannelAccountEntity;
import com.zz.messagepush.support.mapper.ChannelAccountMapper;
import com.zz.messagepush.support.utils.AccountUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Description RefreshGeTuiTokenHandler
 * @Author 张卫刚
 * @Date Created on 2023/4/23
 */

@Service
@Slf4j
public class RefreshGeTuiTokenHandler {

    @Autowired
    private StringRedisTemplate redisTemplate;


    @Autowired
    private ChannelAccountMapper channelAccountMapper;

    @XxlJob("refreshGeTuiAccessTokenJob")
    public void execute() {
        SupportThreadPoolConfig.getPendingSingleThreadPool().execute(() -> {
            List<ChannelAccountEntity> list = channelAccountMapper.findAllByIsDeletedEqualsAndSendChannelEquals(CommonConstant.FALSE, ChannelType.PUSH.getCode());
            for (ChannelAccountEntity account : list) {

                GeTuiAccount geTuiAccount = JSON.parseObject(account.getAccountConfig(), GeTuiAccount.class);
                if (geTuiAccount == null) {
                    break;
                }
                String accessToken = getAccessToken(geTuiAccount);
                if (StrUtil.isNotBlank(accessToken)) {
                    redisTemplate.opsForValue().set(SendAccountConstant.GE_TUI_ACCESS_TOKEN_PREFIX + account.getId(), accessToken);
                }
            }
        });
    }


    private String getAccessToken(GeTuiAccount account) {
        String accessToken = "";

        try {
            String url = "http://restapi.getui.com/v2/" + account.getAppId() + "/auth";
            String time = String.valueOf(System.currentTimeMillis());
            String digest = SecureUtil.sha256().digestHex(account.getAppKey() + time + account.getMasterSecret());

            QueryTokenParamDTO paramDTO = QueryTokenParamDTO.builder().timestamp(time)
                    .appKey(account.getAppKey())
                    .sign(digest).build();

            String body = HttpRequest.post(url).header(Header.CONTENT_TYPE.getValue(), ContentType.JSON.getValue())
                    .body(JSON.toJSONString(paramDTO))
                    .timeout(20000)
                    .execute().body();
            GeTuiTokenResultDTO geTuiTokenResultDTO = JSON.parseObject(body, GeTuiTokenResultDTO.class);

            if (geTuiTokenResultDTO.getCode().equals(0)) {
                accessToken = geTuiTokenResultDTO.getData().getToken();
            }
        } catch (HttpException e) {
            log.error("RefreshGeTuiAccessTokenHandler#getAccessToken fail:{}", Throwables.getStackTraceAsString(e));
        }
        return accessToken;
    }
}
