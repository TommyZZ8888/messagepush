package com.zz.messagepush.support.mq;

import org.springframework.stereotype.Service;

/**
 * @Description SendMqServiceImpl
 * @Author 张卫刚
 * @Date Created on 2023/5/17
 */

@Service
public class SendMqServiceImpl implements SendMqService{
    @Override
    public void send(String topic, String jsonValue, String tagId) {

    }

    @Override
    public void send(String topic, String jsonValue) {

    }
}
