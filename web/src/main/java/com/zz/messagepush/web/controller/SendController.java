package com.zz.messagepush.web.controller;

import com.zz.messagepush.AustinWebApplication;
import io.swagger.annotations.Api;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;

@RestController
@RequestMapping("/push")
@Api("发送消息")
public class SendController {

    @RequestMapping(value = "/hello", method = RequestMethod.GET)
    public String hello() {
        return "ok";
    }
}
