package com.zz.messagepush.support.utils;

import cn.binarywang.wx.miniapp.api.WxMaService;
import cn.binarywang.wx.miniapp.api.WxMaSubscribeService;
import cn.binarywang.wx.miniapp.api.impl.WxMaServiceImpl;
import cn.binarywang.wx.miniapp.api.impl.WxMaSubscribeServiceImpl;
import cn.binarywang.wx.miniapp.config.impl.WxMaDefaultConfigImpl;
import com.alibaba.fastjson.JSON;
import com.zz.messagepush.common.constant.CommonConstant;
import com.zz.messagepush.common.domain.dto.account.WeChatMiniProgramAccount;
import com.zz.messagepush.common.domain.dto.account.WeChatOfficialAccount;
import com.zz.messagepush.common.enums.ChannelType;
import com.zz.messagepush.support.domain.entity.ChannelAccountEntity;
import com.zz.messagepush.support.mapper.ChannelAccountMapper;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.api.impl.WxMpServiceImpl;
import me.chanjar.weixin.mp.config.impl.WxMpDefaultConfigImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Description 微信服务号/小程序 工具类
 * @Author 张卫刚
 * @Date Created on 2023/6/14
 */

@Component
@Slf4j
@Data
public class WxServiceUtil {
    private Map<Long, WxMpService> officialAccountServiceMap = new HashMap<>();
    private Map<Long, WxMaSubscribeService> miniProgramServiceMap = new HashMap<>();

    private Map<Long, WeChatOfficialAccount> officialAccountHashMap = new HashMap<>();
    private Map<Long, WeChatMiniProgramAccount> miniProgramHashMap = new HashMap<>();

    @Autowired
    private ChannelAccountMapper channelAccountMapper;

    @PostConstruct
    public void init() {
        initOfficialAccount();
        initMiniProgram();
    }


    private void initMiniProgram() {
        List<ChannelAccountEntity> miniProgram = channelAccountMapper.findAllByIsDeletedEqualsAndSendChannelEquals(CommonConstant.FALSE, ChannelType.MINI_PROGRAM.getCode());
        for (ChannelAccountEntity channelAccount : miniProgram) {
            WeChatMiniProgramAccount weChatMiniProgramAccount = JSON.parseObject(channelAccount.getAccountConfig(), WeChatMiniProgramAccount.class);
            miniProgramServiceMap.put(channelAccount.getId(), initMiniProgramService(weChatMiniProgramAccount));
            miniProgramHashMap.put(channelAccount.getId(), weChatMiniProgramAccount);
        }
    }

    private void initOfficialAccount() {
        List<ChannelAccountEntity> officialAccountList = channelAccountMapper.findAllByIsDeletedEqualsAndSendChannelEquals(CommonConstant.FALSE, ChannelType.OFFICIAL_ACCOUNT.getCode());
        for (ChannelAccountEntity channelAccount : officialAccountList) {
            WeChatOfficialAccount weChatOfficialAccount = JSON.parseObject(channelAccount.getAccountConfig(), WeChatOfficialAccount.class);
            officialAccountServiceMap.put(channelAccount.getId(), initOfficialAccountService(weChatOfficialAccount));
            officialAccountHashMap.put(channelAccount.getId(), weChatOfficialAccount);
        }
    }


    public WxMpService initOfficialAccountService(WeChatOfficialAccount officialAccount) {
        WxMpServiceImpl wxMpService = new WxMpServiceImpl();
        WxMpDefaultConfigImpl wxMpDefaultConfig = new WxMpDefaultConfigImpl();
        wxMpDefaultConfig.setAppId(officialAccount.getAppId());
        wxMpDefaultConfig.setSecret(officialAccount.getSecret());
        wxMpService.setWxMpConfigStorage(wxMpDefaultConfig);
        return wxMpService;
    }

    /**
     * 初始化微信小程序
     *
     * @return
     */
    private WxMaSubscribeServiceImpl initMiniProgramService(WeChatMiniProgramAccount miniProgramAccount) {
        WxMaService wxMaService = new WxMaServiceImpl();
        WxMaDefaultConfigImpl wxMaConfig = new WxMaDefaultConfigImpl();
        wxMaConfig.setAppid(miniProgramAccount.getAppId());
        wxMaConfig.setSecret(miniProgramAccount.getAppSecret());
        wxMaService.setWxMaConfig(wxMaConfig);
        return new WxMaSubscribeServiceImpl(wxMaService);
    }
}
