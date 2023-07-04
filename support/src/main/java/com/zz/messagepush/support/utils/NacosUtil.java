package com.zz.messagepush.support.utils;

import cn.hutool.core.util.StrUtil;
import com.alibaba.nacos.api.NacosFactory;
import com.alibaba.nacos.api.PropertyKeyConst;
import com.alibaba.nacos.api.annotation.NacosInjected;
import com.alibaba.nacos.api.config.ConfigService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.io.StringReader;
import java.util.Properties;

/**
 * @Description NacosUtil
 * @Author 张卫刚
 * @Date Created on 2023/6/7
 */

@Component
@Slf4j
public class NacosUtil {


    @Value("${austin.nacos.group}")
    private String nacosGroup;

    @Value("${austin.nacos.dataId}")
    private String nacosDataId;

    @NacosInjected
    private ConfigService configService;

    private final Properties properties = new Properties();

    public String getProperty(String key, String defaultValue) {
        try {
            String context = this.getContext();
            if (StringUtils.hasText(context)) {
                properties.load(new StringReader(context));
            }
        } catch (Exception e) {
            log.error("Nacos error: {}", e.getMessage());
        }
        String property = properties.getProperty(key);
        return StrUtil.isBlank(property) ? defaultValue : property;
    }


    private String getContext() {
        String context = null;
        try {
            context = configService.getConfig(nacosDataId, nacosGroup, 5000);
        } catch (Exception e) {
            log.error("Nacos error：{}", e.getMessage());
        }
        return context;
    }
}
