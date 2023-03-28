package com.zz.messagepush.web.controller;

import com.zz.messagepush.common.domain.ResponseResult;
import com.zz.messagepush.support.domain.entity.MessageTemplateEntity;
import com.zz.messagepush.support.mapper.MessageTemplateMapper;
import com.zz.messagepush.web.domain.vo.MessageTemplateVO;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
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


    @RequestMapping(value = "/save", method = RequestMethod.GET)
    public ResponseResult<Boolean> saveOrUpdate(@RequestBody MessageTemplateEntity messageTemplate) {
        int insert = messageTemplateMapper.insert(messageTemplate);
        return ResponseResult.success("insert ok");
    }


    @RequestMapping(value = "/query", method = RequestMethod.GET)
    public ResponseResult<MessageTemplateVO> query() {
        List<MessageTemplateEntity> messageTemplateEntities = messageTemplateMapper.selectList(null);
        MessageTemplateVO build = MessageTemplateVO.builder().rows(messageTemplateEntities).count((long) messageTemplateEntities.size()).build();
        return ResponseResult.success("query ok", build);
    }
}
