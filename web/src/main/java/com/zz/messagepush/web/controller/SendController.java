package com.zz.messagepush.web.controller;

import com.zz.messagepush.common.domain.ResponseResult;
import com.zz.messagepush.common.domain.dto.TaskInfo;
import com.zz.messagepush.common.enums.RespStatusEnum;
import com.zz.messagepush.handler.handler.impl.SmsHandler;
import com.zz.messagepush.service.api.domain.SendResponse;
import com.zz.messagepush.service.api.domain.dto.MessageParam;
import com.zz.messagepush.service.api.domain.dto.SendRequest;
import com.zz.messagepush.service.api.enums.BusinessCode;
import com.zz.messagepush.service.api.service.SendService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;

@RestController
@RequestMapping("/message")
@Api("发送消息")
public class SendController {

    @Autowired
    private SendService sendService;

    @Autowired
    private SmsHandler smsHandler;

    @RequestMapping(value = "/send", method = RequestMethod.POST)
    @ApiOperation("消息下发，多渠道多类型下发消息，目前支持邮件和短信，类型支持：验证码、通知类、营销类")
    public ResponseResult<Boolean> send(@RequestBody SendRequest sendRequest) {
        sendService.send(sendRequest);
        return ResponseResult.success(RespStatusEnum.SUCCESS.getDescription());
    }


    @RequestMapping(value = "/sendSms", method = RequestMethod.POST)
    public ResponseResult<Boolean> sendSms(String phone, Long messageTemplateId) {
        HashMap<String, String> map = new HashMap<>();
        map.put("contentValue", "6666");
        MessageParam messageParam = new MessageParam().setReceiver(phone).setVariables(map);

        SendRequest sendRequest = new SendRequest().setCode(BusinessCode.COMMON_SEND.getCode())
                .setMessageTemplateId(messageTemplateId)
                .setMessageParam(messageParam);
        sendService.send(sendRequest);
        return ResponseResult.success(RespStatusEnum.SUCCESS.getDescription());
    }
}
