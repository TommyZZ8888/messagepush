package com.zz.messagepush.web.controller;

import com.zz.messagepush.support.mapper.SmsRecordMapper;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Description
 * @Author 张卫刚
 * @Date Created on 2023/3/14
 */

@RestController
@RequestMapping("/smsRecord")
public class SmsRecordController {

    @Autowired
    private SmsRecordMapper smsRecordMapper;

    @ApiOperation("testInsert")
    @RequestMapping(value = "/insert",method = RequestMethod.POST)
    public String insert(String phone){
        return "";
    }

    @RequestMapping(value = "/query",method = RequestMethod.POST)
    public String query(){
        return "";
    }
}
