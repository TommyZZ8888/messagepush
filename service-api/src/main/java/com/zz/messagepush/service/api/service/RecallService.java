package com.zz.messagepush.service.api.service;

import com.zz.messagepush.service.api.domain.SendResponse;
import com.zz.messagepush.service.api.domain.dto.SendRequest;

/**
 * 撤回接口
 * @author DELL
 */
public interface RecallService {
    /**
     * 撤回消息（根据模板id）
     * @param sendRequest
     * @return
     */
    SendResponse recall(SendRequest sendRequest);
}
