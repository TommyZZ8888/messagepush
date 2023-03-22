package com.zz.messagepush.handler.handler;

import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * @Description  channel--》handler的映射
 * @Author 张卫刚
 * @Date Created on 2023/3/22
 */
@Component
public class HandlerHolder {

    private Map<Integer, Handler> handlerMap = new HashMap<>();

    public void putHandler(Integer channelCode, Handler handler) {
        handlerMap.put(channelCode, handler);
    }

    public Handler route(Integer channelCode) {
        return handlerMap.get(channelCode);
    }

}
