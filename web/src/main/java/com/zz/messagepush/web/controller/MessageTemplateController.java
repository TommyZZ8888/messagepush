package com.zz.messagepush.web.controller;

import cn.hutool.core.util.StrUtil;
import com.zz.messagepush.common.domain.PageResult;
import com.zz.messagepush.common.domain.ResponseResult;
import com.zz.messagepush.support.domain.dto.MessageTemplateParamDTO;
import com.zz.messagepush.support.domain.entity.MessageTemplateEntity;
import com.zz.messagepush.support.mapper.MessageTemplateMapper;
import com.zz.messagepush.support.domain.vo.MessageTemplateVO;
import com.zz.messagepush.support.service.MessageTemplateService;
import com.zz.messagepush.support.utils.ConvertMap;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.util.Arrays;
import java.util.List;
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

    private static final List<String> flatFieldName = Arrays.asList("msgContent");
    @Autowired
    private MessageTemplateService messageTemplateService;


    @RequestMapping(value = "/save", method = RequestMethod.GET)
    public ResponseResult<Boolean> saveOrUpdate(@RequestBody MessageTemplateParamDTO dto) {
        messageTemplateService.saveOrUpdate(dto);
        return ResponseResult.success("insert ok");
    }


    @RequestMapping(value = "/query", method = RequestMethod.GET)
    public ResponseResult<MessageTemplateVO> queryList(@RequestBody MessageTemplateParamDTO dto) throws IllegalAccessException {
        PageResult<MessageTemplateEntity> messageTemplateEntities = messageTemplateService.queryNotDeletedList(dto);
        MessageTemplateVO build = MessageTemplateVO.builder().rows(ConvertMap.flatFirst(messageTemplateEntities.getList(),flatFieldName)).count((long) messageTemplateEntities.getList().size()).build();
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
        messageTemplateService.delete(idList);
        return ResponseResult.success();
    }
}
