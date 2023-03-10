package com.zz.messagepush.service.api.service;

import com.zz.messagepush.service.api.domain.dto.SendRequest;

public interface RecallService {
    /**
     * 撤回消息
     * @param sendRequest
     * @return
     */
    boolean recall(SendRequest sendRequest);
}
