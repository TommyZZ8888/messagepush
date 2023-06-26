package com.zz.messagepush.support.mq.springeventbus;


import org.springframework.context.ApplicationEvent;

import java.time.Clock;

/**
 * @Description SpringEventBusEvent
 * @Author 张卫刚
 * @Date Created on 2023/6/25
 */

public class SpringEventBusEvent extends ApplicationEvent{

    public String jsonValue;

    public String tagId;

    public String topic;

    public SpringEventBusEvent(Object source,String jsonValue, String tagId, String topic) {
        super(source);
        this.jsonValue = jsonValue;
        this.tagId = tagId;
        this.topic = topic;
    }

    public String getJsonValue() {
        return jsonValue;
    }

    public void setJsonValue(String jsonValue) {
        this.jsonValue = jsonValue;
    }

    public String getTagId() {
        return tagId;
    }

    public void setTagId(String tagId) {
        this.tagId = tagId;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }
}
