package com.zz.messagepush.web.controller;

import com.zz.messagepush.common.domain.ResponseResult;
import com.zz.messagepush.support.domain.entity.MessageTemplateEntity;
import com.zz.messagepush.support.mapper.MessageTemplateMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.List;

/**
 * @Description
 * @Author 张卫刚
 * @Date Created on 2023/3/14
 */
@RestController
@RequestMapping("/messageTemplate")
public class MessageTemplateController {


    @Autowired
    private MessageTemplateMapper messageTemplateMapper;


    @RequestMapping(value = "/insert", method = RequestMethod.GET)
    public ResponseResult<Boolean> insert() {
        MessageTemplateEntity build = MessageTemplateEntity.builder()
                .name("test短信")
                .auditStatus(10)
                .flowId("yyyy")
                .msgStatus(10)
                .idType(10)
                .sendChannel(10)
                .templateType(10)
                .msgType(10)
                .expectPushTime("0")
                .msgContent("3333333m")
                .sendAccount(66)
                .creator("yyyyc")
                .updator("yyyyu")
                .team("yyyt")
                .proposer("yyyy22")
                .auditor("yyyyyyz")
                .isDeleted(0)
                .created(new Date())
                .build();

        int insert = messageTemplateMapper.insert(build);
        return ResponseResult.success("insert ok");
    }


    @RequestMapping(value = "/query", method = RequestMethod.GET)
    public ResponseResult<List<MessageTemplateEntity>> query() {
        return ResponseResult.success("query ok",messageTemplateMapper.selectList(null));
    }
}
