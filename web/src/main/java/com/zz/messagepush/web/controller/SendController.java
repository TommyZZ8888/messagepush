package com.zz.messagepush.web.controller;

import com.zz.messagepush.AustinWebApplication;
import com.zz.messagepush.common.domain.ResponseResult;
import com.zz.messagepush.common.domain.dto.TaskInfoDTO;
import com.zz.messagepush.common.enums.RespStatusEnum;
import com.zz.messagepush.handler.handler.impl.SmsHandler;
import com.zz.messagepush.service.api.domain.dto.SendRequest;
import com.zz.messagepush.service.api.service.SendService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;
import java.util.Arrays;
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
        return ResponseResult.success(RespStatusEnum.SUCCESS.getMsg());
    }


    @RequestMapping(value = "/sendSms", method = RequestMethod.POST)
    public boolean sendSms(String phone, String content, Long messageTemplateId) {
        TaskInfoDTO build = TaskInfoDTO.builder().receiver(new HashSet<>(Arrays.asList(phone)))
                .content(content).messageTemplateId(messageTemplateId).build();
        return smsHandler.doHandler(build);
    }
}
