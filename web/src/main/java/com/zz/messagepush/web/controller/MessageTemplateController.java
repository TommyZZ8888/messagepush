package com.zz.messagepush.web.controller;

import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import com.zz.messagepush.common.constant.AustinConstant;
import com.zz.messagepush.common.constant.CommonConstant;
import com.zz.messagepush.common.domain.ResponseResult;
import com.zz.messagepush.common.enums.RespStatusEnum;
import com.zz.messagepush.service.api.domain.SendResponse;
import com.zz.messagepush.service.api.domain.dto.SendRequest;
import com.zz.messagepush.service.api.enums.BusinessCode;
import com.zz.messagepush.service.api.service.RecallService;
import com.zz.messagepush.service.api.service.SendService;
import com.zz.messagepush.support.domain.dto.MessageTemplateParamDTO;
import com.zz.messagepush.support.domain.entity.MessageTemplateEntity;
import com.zz.messagepush.support.domain.vo.MessageTemplateVO;
import com.zz.messagepush.web.service.MessageTemplateService;
import com.zz.messagepush.support.utils.ConvertMap;
import com.zz.messagepush.web.utils.Convert4Amis;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.validation.constraints.NotBlank;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;


/**
 * @Description
 * @Author 张卫刚
 * @Date Created on 2023/3/14
 */
@RestController
@RequestMapping("/messageTemplate")
@Validated
public class MessageTemplateController {

    private static final List<String> FLAT_FIELD_NAME = Collections.singletonList("msgContent");

    @Value("${austin.business.upload.crowd.path}")
    private String dataPath;
    @Autowired
    private MessageTemplateService messageTemplateService;


    @Autowired
    private SendService sendService;

    @Resource
    private RecallService recallService;


    @RequestMapping(value = "/save", method = RequestMethod.GET)
    public ResponseResult<Boolean> saveOrUpdate(@RequestBody MessageTemplateParamDTO dto) {
        messageTemplateService.saveOrUpdate(dto);
        return ResponseResult.success("insert ok");
    }


    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public ResponseResult<MessageTemplateVO> queryList(@Validated @RequestBody MessageTemplateParamDTO dto) throws IllegalAccessException {
        Page<MessageTemplateEntity> messageTemplates = messageTemplateService.queryList(dto);
        List<Map<String, Object>> result = Convert4Amis.flatListMap(messageTemplates.toList());
        MessageTemplateVO messageTemplateVo = MessageTemplateVO.builder().count(messageTemplates.getTotalElements()).rows(result).build();
        return ResponseResult.success("query ok", messageTemplateVo);
    }


    @RequestMapping(value = "/queryById", method = RequestMethod.GET)
    public ResponseResult<Map<String, Object>> queryById(Long id) throws IllegalAccessException {
        return ResponseResult.success("query ok", ConvertMap.flatSingle(messageTemplateService.queryById(id)));
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
    @ApiOperation("根据Id删除")
    @RequestMapping(value = "/deleteById", method = RequestMethod.POST)
    public ResponseResult<Boolean> deleteByIds(@RequestParam(value = "id", required = false) @NotBlank(message = "id不能为空") String id) {
        List<Long> idList = Arrays.stream(id.split(StrUtil.COMMA)).map(Long::valueOf).collect(Collectors.toList());
        messageTemplateService.deleteByIds(idList);
        return ResponseResult.success();
    }


    @ApiOperation("启动模板的定时任务")
    @RequestMapping(value = "/start", method = RequestMethod.POST)
    public ResponseResult start(@RequestParam(value = "id", required = false) @NotBlank(message = "id不能为空") Long id) {
        return messageTemplateService.startCronTask(id);
    }


    @ApiOperation("暂停模板的定时任务")
    @RequestMapping(value = "/stop", method = RequestMethod.POST)
    public ResponseResult<Boolean> stop(@RequestParam(value = "id", required = false) @NotBlank(message = "id不能为空") Long id) {
        messageTemplateService.stopCronTask(id);
        return ResponseResult.success();
    }


    @ApiOperation("上传人群文件")
    @RequestMapping(value = "/upload", method = RequestMethod.POST)
    public ResponseResult<Boolean> upload(@RequestParam("file") MultipartFile file) {
        String path = dataPath + IdUtil.fastSimpleUUID() + file.getOriginalFilename();

        try {
            File localFile = new File(path);
            if (!localFile.exists()) {
                boolean mkdir = localFile.mkdir();
            }
            file.transferTo(localFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return ResponseResult.success();
    }


    @ApiOperation("撤回消息")
    @RequestMapping(value = "/recall", method = RequestMethod.POST)
    public ResponseResult<Boolean> recall(@RequestParam("id") String id) {
        SendRequest sendRequest = SendRequest.builder().messageTemplateId(Long.valueOf(id)).code(BusinessCode.RECALL_SEND.getCode()).build();
        SendResponse response = recallService.recall(sendRequest);
        if (!Objects.equals(response.getCode(), RespStatusEnum.SUCCESS.getCode())) {
            return ResponseResult.fail("recall fail");
        }
        return ResponseResult.success();
    }
}
