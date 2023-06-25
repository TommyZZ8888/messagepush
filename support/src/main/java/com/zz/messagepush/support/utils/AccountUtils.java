package com.zz.messagepush.support.utils;

import cn.binarywang.wx.miniapp.api.WxMaService;
import cn.binarywang.wx.miniapp.api.impl.WxMaServiceImpl;
import cn.binarywang.wx.miniapp.api.impl.WxMaSubscribeServiceImpl;
import cn.binarywang.wx.miniapp.config.impl.WxMaDefaultConfigImpl;
import cn.binarywang.wx.miniapp.config.impl.WxMaRedisBetterConfigImpl;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.nacos.shaded.com.google.common.base.Throwables;
import com.ctrip.framework.apollo.Config;
import com.ctrip.framework.apollo.spring.annotation.ApolloConfig;
import com.taobao.api.internal.toplink.channel.ChannelTimeoutException;
import com.zz.messagepush.common.constant.AustinConstant;
import com.zz.messagepush.common.constant.CommonConstant;
import com.zz.messagepush.common.constant.SendAccountConstant;
import com.zz.messagepush.common.domain.dto.account.WeChatMiniProgramAccount;
import com.zz.messagepush.common.domain.dto.account.WeChatOfficialAccount;
import com.zz.messagepush.common.domain.dto.account.sms.SmsAccount;
import com.zz.messagepush.common.enums.ChannelType;
import com.zz.messagepush.support.domain.entity.ChannelAccountEntity;
import com.zz.messagepush.support.mapper.ChannelAccountMapper;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.redis.RedisTemplateWxRedisOps;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.api.impl.WxMpServiceImpl;
import me.chanjar.weixin.mp.config.impl.WxMpDefaultConfigImpl;
import me.chanjar.weixin.mp.config.impl.WxMpRedisConfigImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Description
 * @Author 张卫刚
 * @Date Created on 2023/3/28
 */

@Component
@Slf4j
public class AccountUtils {

    @ApolloConfig("boss.austin")
    private Config config;

    @Autowired
    private ChannelAccountMapper channelAccountMapper;


    @Autowired
    private StringRedisTemplate redisTemplate;


    private Map<ChannelAccountEntity, WxMaService> miniProgramServiceMap = new ConcurrentHashMap<>();
    private Map<ChannelAccountEntity, WxMpService> officialAccountMap = new ConcurrentHashMap<>();

    public RedisTemplateWxRedisOps redisTemplateWxRedisOps() {
        return new RedisTemplateWxRedisOps(redisTemplate);
    }


    /**
     * @param sendAccountId
     * @param clazz
     * @param <T>
     * @return
     */
    public <T> T getAccountById(Integer sendAccountId, Class<T> clazz) {
        //优先都数据库的，数据库没有才读配置
        try {
            Optional<ChannelAccountEntity> accountEntity = channelAccountMapper.findById(Long.valueOf(sendAccountId));
            if (accountEntity.isPresent()) {
                ChannelAccountEntity channelAccountEntity = accountEntity.get();
                if (clazz.equals(WxMpService.class)) {
                    return (T) officialAccountMap.computeIfAbsent(channelAccountEntity, account -> initOfficialAccountService(JSON.parseObject(account.getAccountConfig(), WeChatOfficialAccount.class)));
                }
                if (clazz.equals(WxMaService.class)) {
                    return (T) miniProgramServiceMap.computeIfAbsent(channelAccountEntity, account -> initMiniProgramService(JSON.parseObject(account.getAccountConfig(), WeChatMiniProgramAccount.class)));
                } else {
                    return JSON.parseObject(channelAccountEntity.getAccountConfig(), clazz);
                }
            }
        } catch (Exception e) {
            log.warn("AccountUtil#getAccount not found:{}", Throwables.getStackTraceAsString(e));
        }
        return null;
    }


    public <T> T getSmsAccountByScriptName(String scriptName, Class<T> clazz) {
        List<ChannelAccountEntity> entityList = channelAccountMapper.findAllByIsDeletedEqualsAndSendChannelEquals(CommonConstant.FALSE, ChannelType.SMS.getCode());
        for (ChannelAccountEntity channelAccountEntity : entityList) {
            SmsAccount smsAccount = JSON.parseObject(channelAccountEntity.getAccountConfig(), SmsAccount.class);
            if (smsAccount.getScriptName().equals(scriptName)) {
                return JSON.parseObject(channelAccountEntity.getAccountConfig(), clazz);
            }
        }
        return null;
    }


    public WxMpService initOfficialAccountService(WeChatOfficialAccount officialAccount) {
        WxMpServiceImpl wxMpService = new WxMpServiceImpl();
        WxMpDefaultConfigImpl wxMpDefaultConfig = new WxMpRedisConfigImpl(redisTemplateWxRedisOps(), SendAccountConstant.OFFICIAL_ACCOUNT_ACCESS_TOKEN_PREFIX);
        wxMpDefaultConfig.setAppId(officialAccount.getAppId());
        wxMpDefaultConfig.setSecret(officialAccount.getSecret());
        wxMpDefaultConfig.setToken(officialAccount.getToken());
        wxMpService.setWxMpConfigStorage(wxMpDefaultConfig);
        return wxMpService;
    }

    /**
     * 初始化微信小程序
     * access_token 用redis存储
     * @return
     */
    private WxMaService initMiniProgramService(WeChatMiniProgramAccount miniProgramAccount) {
        WxMaService wxMaService = new WxMaServiceImpl();
        WxMaRedisBetterConfigImpl config = new WxMaRedisBetterConfigImpl(redisTemplateWxRedisOps(), SendAccountConstant.MINI_PROGRAM_TOKEN_PREFIX);
        config.setAppid(miniProgramAccount.getAppId());
        config.setSecret(miniProgramAccount.getAppSecret());
        wxMaService.setWxMaConfig(config);
        return wxMaService;
    }

}
