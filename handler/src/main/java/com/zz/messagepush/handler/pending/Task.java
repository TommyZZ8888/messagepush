package com.zz.messagepush.handler.pending;

import com.zz.messagepush.common.domain.dto.TaskInfo;
import com.zz.messagepush.handler.handler.HandlerHolder;
import lombok.Data;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @Description task执行器，1.通用去重  2.发送消息
 * @Author 张卫刚
 * @Date Created on 2023/3/22
 */


@Data
@Accessors(chain = true)
@Slf4j
public class Task implements Runnable {

    @Autowired
    private HandlerHolder handlerHolder;


    private TaskInfo taskInfo;


    //TODO 丢弃信息

    //TODO 通用去重


    @Override
    public void run() {
        //发送消息
        handlerHolder.route(taskInfo.getSendChannel()).doHandler(taskInfo);
    }
}
