package com.zz.messagepush.handler.script;

import cn.hutool.core.util.ArrayUtil;
import com.zz.messagepush.handler.script.anno.SmsScriptHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;
import java.lang.annotation.Annotation;

/**
 * @Description sms发送脚本的抽象类
 * @Author 张卫刚
 * @Date Created on 2023/5/4
 */
@Slf4j
public abstract class BaseSmsScript implements SmsService {

    @Autowired
    private SmsScriptHolder smsScriptHolder;

    @PostConstruct
    public void registryProcessScript() {
        if (ArrayUtil.isEmpty(this.getClass().getAnnotations())) {
            log.error("baseSmsScript not find anno");
            return;
        }
        Annotation handlerAnnotation = null;
        for (Annotation annotation : this.getClass().getAnnotations()) {
            if (annotation instanceof SmsScriptHandler) {
                handlerAnnotation = annotation;
                break;
            }
        }
        if (handlerAnnotation == null) {
            log.error("handler anno not declared");
            return;
        }
        //注册handler
        smsScriptHolder.putHandler(((SmsScriptHandler) handlerAnnotation).value(), this);
    }
}
