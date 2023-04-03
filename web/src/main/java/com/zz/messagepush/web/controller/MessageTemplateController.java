package com.zz.messagepush.web.controller;

import cn.hutool.core.util.StrUtil;
import com.zz.messagepush.common.domain.ResponseResult;
import com.zz.messagepush.service.api.service.SendService;
import com.zz.messagepush.support.domain.dto.MessageTemplateParamDTO;
import com.zz.messagepush.support.domain.entity.MessageTemplateEntity;
import com.zz.messagepush.support.domain.vo.MessageTemplateVO;
import com.zz.messagepush.web.service.MessageTemplateService;
import com.zz.messagepush.support.utils.ConvertMap;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotBlank;
import java.util.*;
import java.util.stream.Collectors;


/**
 * @Description
 * @Author 张卫刚
 * @Date Created on 2023/3/14
 */
@RestController
@RequestMapping("/messageTemplate")
@CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true", allowedHeaders = "")
@Validated
public class MessageTemplateController {

    private static final List<String> FLAT_FIELD_NAME = Collections.singletonList("msgContent");
    @Autowired
    private MessageTemplateService messageTemplateService;


    @Autowired
    private SendService sendService;


    @RequestMapping(value = "/save", method = RequestMethod.GET)
    public ResponseResult<Boolean> saveOrUpdate(@RequestBody MessageTemplateParamDTO dto) {
        messageTemplateService.saveOrUpdate(dto);
        return ResponseResult.success("insert ok");
    }


    @RequestMapping(value = "/query", method = RequestMethod.GET)
    public ResponseResult<MessageTemplateVO> queryList(@RequestBody MessageTemplateParamDTO dto) throws IllegalAccessException {
        List<MessageTemplateEntity> messageTemplateEntities = messageTemplateService.queryNotDeletedList(dto);
        MessageTemplateVO build = MessageTemplateVO.builder().rows(ConvertMap.flatFirst(messageTemplateEntities,FLAT_FIELD_NAME)).count((long) messageTemplateEntities.size()).build();
        return ResponseResult.success("query ok", build);
    }


    @RequestMapping(value = "/queryById", method = RequestMethod.GET)
    public ResponseResult<MessageTemplateEntity> queryById(Long id) {
        return ResponseResult.success("query ok", messageTemplateService.queryById(id));
    }


    /**
     * 根据Id复制
     */
    @RequestMapping(value = "/copy", method = RequestMethod.POST)
    public ResponseResult<Boolean> copyById(@RequestParam(value = "id", required = false) @NotBlank(message = "ID不能为空") Long id) {
        messageTemplateService.copy(id);
        return ResponseResult.success();
    }


    /**
     * 根据Id删除
     * id多个用逗号分隔开
     */
    @RequestMapping(value = "/deleteById", method = RequestMethod.POST)
    public ResponseResult<Boolean> deleteByIds(@RequestParam(value = "id", required = false) @NotBlank(message = "id不能为空") String id) {
        List<Long> idList = Arrays.stream(id.split(StrUtil.COMMA)).map(Long::valueOf).collect(Collectors.toList());
        messageTemplateService.deleteByIds(idList);
        return ResponseResult.success();
    }



    @RequestMapping(value = "/start",method = RequestMethod.POST)
    public ResponseResult start(@RequestParam(value = "id",required = false) @NotBlank(message = "id不能为空") Long id){
        return messageTemplateService.startCronTask(id);
    }

    @RequestMapping(value = "/stop",method = RequestMethod.POST)
    public ResponseResult<Boolean> stop(@RequestParam(value = "id",required = false) @NotBlank(message = "id不能为空") Long id){
        messageTemplateService.stopCronTask(id);
        return ResponseResult.success();
    }
}
