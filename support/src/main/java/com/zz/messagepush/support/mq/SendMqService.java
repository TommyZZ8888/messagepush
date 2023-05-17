package com.zz.messagepush.support.mq;

/**
 * @Description 发送消息到消息队列
 * @Author 张卫刚
 * @Date Created on 2023/5/17
 */
public interface SendMqService {

    /**
     * 发送消息
     * @param topic
     * @param jsonValue
     * @param tagId
     */
    void send(String topic,String jsonValue,String tagId);

    /**
     * 发送消息
     * @param topic
     * @param jsonValue
     */
    void send(String topic,String jsonValue);
}
