package com.zz.messagepush.handler.script;

import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * @Description sendAccount==>SmsService的映射关系
 * @Author 张卫刚
 * @Date Created on 2023/5/4
 */
@Component
public class SmsScriptHolder {

    private final Map<String, SmsService> handlers = new HashMap<>(8);

    public void putHandler(String scriptName, SmsService smsService) {
        handlers.put(scriptName, smsService);
    }

    public SmsService route(String scriptName) {
        return handlers.get(scriptName);
    }
}
