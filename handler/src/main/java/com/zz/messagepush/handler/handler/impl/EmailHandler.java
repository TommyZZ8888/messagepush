package com.zz.messagepush.handler.handler.impl;

import com.zz.messagepush.common.domain.dto.TaskInfo;
import com.zz.messagepush.common.enums.ChannelType;
import com.zz.messagepush.handler.handler.Handler;
import org.springframework.context.annotation.Configuration;

/**
 * @Description 邮件发送处理
 * @Author 张卫刚
 * @Date Created on 2023/3/22
 */

@Configuration
public class EmailHandler extends Handler {


    public EmailHandler(){
         channelCode = ChannelType.EMAIL.getCode();
    }

    @Override
    public void doHandler(TaskInfo taskInfo) {

    }

    @Override
    public void handler(TaskInfo taskInfo) {

    }
}
