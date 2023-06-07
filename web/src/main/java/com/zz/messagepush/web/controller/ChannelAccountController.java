package com.zz.messagepush.web.controller;

import com.zz.messagepush.common.domain.ResponseResult;
import com.zz.messagepush.support.domain.entity.ChannelAccountEntity;
import com.zz.messagepush.web.service.ChannelAccountService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.bouncycastle.cert.ocsp.Req;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @Description ChannelAccountController
 * @Author 张卫刚
 * @Date Created on 2023/6/7
 */

@Api("素材管理接口")
@Slf4j
@RestController
@RequestMapping("/account")
@CrossOrigin(origins = "http://localhost:3000", allowedHeaders = "*", allowCredentials = "true")
public class ChannelAccountController {


    @Autowired
    private ChannelAccountService channelAccountService;


    @ApiOperation("保存数据")
    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public ResponseResult<Boolean> saveOrUpdate(@RequestBody ChannelAccountEntity channelAccountEntity) {
        channelAccountService.save(channelAccountEntity);
        return ResponseResult.success("success");
    }


    @ApiOperation("查询数据")
    @RequestMapping(value = "/query", method = RequestMethod.POST)
    public ResponseResult<Boolean> query(Integer channelType) {
        channelAccountService.queryByChannelType(channelType);
        return ResponseResult.success();
    }

}
