package com.zz.messagepush.handler.handler;

import com.zz.messagepush.common.domain.dto.TaskInfo;
import com.zz.messagepush.common.enums.ChannelType;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;

/**
 * @Description
 * @Author 张卫刚
 * @Date Created on 2023/3/13
 */
public abstract class Handler {


    @Autowired
    private HandlerHolder handlerHolder;

    @PostConstruct
    private void init() {
        for (ChannelType channelType : ChannelType.values()) {
            handlerHolder.putHandler(channelType.getCode(), this);
        }
    }

    /**
     * 消息处理
     *
     * @param taskInfo
     * @return
     */
    public void doHandler(TaskInfo taskInfo) {
        handler(taskInfo);
    }

    /**
     * 统一处理的下发接口
     *
     * @param taskInfo
     */
    public abstract void handler(TaskInfo taskInfo);
}
