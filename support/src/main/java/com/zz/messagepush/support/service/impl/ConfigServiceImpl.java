package com.zz.messagepush.support.service.impl;

import cn.hutool.core.util.StrUtil;
import cn.hutool.setting.dialect.Props;
import com.ctrip.framework.apollo.Config;
import com.zz.messagepush.support.service.ConfigService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * @Description 读取本地配置实现类
 * @Author 张卫刚
 * @Date Created on 2023/5/12
 */
@Service
public class ConfigServiceImpl implements ConfigService {

    /**
     * 本地配置
     */
    private static final String PROPERTIES_PATH = "local.properties";
    private final Props props  = new Props(PROPERTIES_PATH);

    @Value("${apollo.bootstrap.enabled}")
    private Boolean enableApollo;

    @Value("${apollo.bootstrap.namespace}")
    private String nameSpaces;


    @Override
    public String getProperty(String key, String defaultValue) {
        if (enableApollo) {
            Config config = com.ctrip.framework.apollo.ConfigService.getConfig(nameSpaces.split(StrUtil.COMMA)[0]);
            return config.getProperty(key,defaultValue);
        }else {
            return props.getProperty(key,defaultValue);
        }
    }

}
