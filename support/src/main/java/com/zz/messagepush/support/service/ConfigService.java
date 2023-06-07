package com.zz.messagepush.support.service;

/**
 * @Description ConfigService
 * @Author 张卫刚
 * @Date Created on 2023/5/12
 */
public interface ConfigService {

    /**
     * 读取配置
     * 1.当启动了apollo或者nacos，优先读取远程配置
     * 2.当没有启动远程配置，读取本地local.properties的配置
     * @param key
     * @param defaultValue
     * @return
     */
    String getProperty(String key,String defaultValue);
}
