package com.zz.messagepush.service.api.impl.service;

import com.zz.messagepush.service.api.domain.dto.BatchSendRequest;
import com.zz.messagepush.service.api.domain.dto.SendRequest;
import com.zz.messagepush.service.api.service.SendService;
import org.springframework.stereotype.Service;


@Service
public class SendServiceImpl implements SendService {


    @Override
    public boolean send(SendRequest sendRequest) {

        return false;
    }

    @Override
    public boolean batchSend(BatchSendRequest sendRequest) {

        return false;
    }
}
