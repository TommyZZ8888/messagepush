package com.zz.messagepush.web.controller;

import com.zz.messagepush.common.domain.ResponseResult;
import com.zz.messagepush.common.enums.RespStatusEnum;
import com.zz.messagepush.support.utils.RedisUtil;
import com.zz.messagepush.web.domain.vo.DataParamVO;
import com.zz.messagepush.web.domain.vo.amis.EchartsVO;
import com.zz.messagepush.web.domain.vo.amis.SmsTimeLineVO;
import com.zz.messagepush.web.domain.vo.amis.UserTimeLineVO;
import com.zz.messagepush.web.service.DataService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotBlank;

/**
 * @Description 获取数据接口 == 全链路追踪
 * @Author 张卫刚
 * @Date Created on 2023/4/10
 */
@RequestMapping("/data")
@RestController
@Slf4j
@CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true", allowedHeaders = "")
public class DataController {

    @Autowired
    private RedisUtil redisUtil;


    @Autowired
    private DataService dataService;

    /**
     * 如果id存在则则修改
     * 不存在则保存
     *
     * @param dataParamVO
     * @return
     */
    @RequestMapping(value = "/getData", method = RequestMethod.POST)
    public ResponseResult<SmsTimeLineVO> getData(@RequestBody DataParamVO dataParamVO) {
        if (dataParamVO == null || dataParamVO.getDateTime() == null || dataParamVO.getReceiver() == null) {
            return ResponseResult.success(RespStatusEnum.SUCCESS.getCode(), new SmsTimeLineVO());
        }
        SmsTimeLineVO smsTimeLineVo = dataService.getTraceSmsInfo(dataParamVO);
        return ResponseResult.success(RespStatusEnum.SUCCESS.getCode(), smsTimeLineVo);
    }

    @RequestMapping(value = "/getUserData", method = RequestMethod.POST)
    public ResponseResult<UserTimeLineVO> getUserData(@RequestParam("receiver") @NotBlank(message = "receiver不能为空") String receiver) {
        UserTimeLineVO traceUserInfo = dataService.getTraceUserInfo(receiver);
        return ResponseResult.success("getUserData", traceUserInfo);
    }


    @RequestMapping(value = "/getMessageTemplateInfo", method = RequestMethod.POST)
    public ResponseResult<EchartsVO> getMessageTemplateInfo(@RequestParam("businessId") @NotBlank(message = "businessId不能为空") String businessId) {
        EchartsVO echartsVO = dataService.getTraceMessageTemplateInfo(businessId);
        return ResponseResult.success("getMessageTemplateInfo", echartsVO);
    }
}
