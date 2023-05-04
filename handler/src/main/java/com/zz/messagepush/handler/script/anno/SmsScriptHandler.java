package com.zz.messagepush.handler.script.anno;

import org.springframework.stereotype.Component;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @Description 标识短信渠道
 * @Author 张卫刚
 * @Date Created on 2023/5/4
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
@Component
public @interface SmsScriptHandler {

    /**
     * 输入脚本名
     * @return
     */
    String value();
}
