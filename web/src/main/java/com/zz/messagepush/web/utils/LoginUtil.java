package com.zz.messagepush.web.utils;

import com.zz.messagepush.common.constant.CommonConstant;
import com.zz.messagepush.common.constant.OfficialAccountParamConstant;
import com.zz.messagepush.common.utils.ApplicationContextUtil;
import com.zz.messagepush.web.config.WeChatLoginAccountConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Objects;

/**
 * @Description LoginUtil
 * @Author 张卫刚
 * @Date Created on 2023/6/19
 */

@Component
@Slf4j
public class LoginUtil {

    @Value("${spring.profiles.active}")
    private String env;

    public WeChatLoginAccountConfig getLoginConfig() {
        try {
            return ApplicationContextUtil.getBean(OfficialAccountParamConstant.WE_CHAT_LOGIN_CONFIG, WeChatLoginAccountConfig.class);
        } catch (Exception e) {
            return null;
        }
    }

    public Boolean needLogin() {
        try {
            WeChatLoginAccountConfig loginConfig = getLoginConfig();
            if (CommonConstant.ENV_TEST.equals(env) && Objects.nonNull(loginConfig)) {
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}
