package com.zz.messagepush.web.controller;

import com.alibaba.fastjson.JSON;
import com.zz.messagepush.common.domain.ResponseResult;
import com.zz.messagepush.support.domain.entity.MessageTemplateEntity;
import com.zz.messagepush.support.utils.RedisUtil;
import com.zz.messagepush.web.domain.vo.DataParamVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

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

    /**
     * 如果id存在则则修改
     * 不存在则保存
     *
     * @param dataParamVO
     * @return
     */
    @RequestMapping(value = "/getData", method = RequestMethod.POST)
    public ResponseResult<Boolean> getData(@RequestBody DataParamVO dataParamVO) {
        Long businessId = dataParamVO.getBusinessId();
        Map<Object, Object> objectObjectMap = redisUtil.hGetAll(String.valueOf(businessId));
        log.info("data get:{}", JSON.toJSONString(objectObjectMap));
        return ResponseResult.success();
    }
}
