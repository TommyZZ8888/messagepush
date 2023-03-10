package com.zz.messagepush.service.api.service;

import com.zz.messagepush.service.api.domain.dto.BatchSendRequest;
import com.zz.messagepush.service.api.domain.dto.SendRequest;

import javax.management.BadAttributeValueExpException;

public interface SendService {

    /**
     * 单条消息发送
     * @param sendRequest
     * @return
     */
    boolean send(SendRequest sendRequest);

    /**
     * 多条消息发送
     * @param sendRequest
     * @return
     */
    boolean batchSend(BatchSendRequest sendRequest);

}
