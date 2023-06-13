package com.zz.messagepush.web.controller;

import cn.hutool.core.util.StrUtil;
import com.alibaba.nacos.common.tls.SelfTrustManager;
import com.zz.messagepush.common.constant.AustinConstant;
import com.zz.messagepush.common.domain.ResponseResult;
import com.zz.messagepush.support.domain.entity.ChannelAccountEntity;
import com.zz.messagepush.web.service.ChannelAccountService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.bouncycastle.cert.ocsp.Req;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Description ChannelAccountController
 * @Author 张卫刚
 * @Date Created on 2023/6/7
 */

@Api("素材管理接口")
@Slf4j
@RestController
@RequestMapping("/account")
@CrossOrigin(origins = AustinConstant.ORIGIN_VALUE, allowedHeaders = "*", allowCredentials = "true")
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

    @ApiOperation("渠道账户列表信息")
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public ResponseResult<List<ChannelAccountEntity>> list() {
        return ResponseResult.success("ok", channelAccountService.list());
    }


    @ApiOperation("根据ids删除")
    @RequestMapping(value = "/deleteByIds", method = RequestMethod.POST)
    public ResponseResult<Boolean> deleteByIds(String id) {
        if (StringUtils.isNotBlank(id)) {
            List<Long> collect = Arrays.stream(id.split(StrUtil.COMMA)).map(Long::valueOf).collect(Collectors.toList());
            channelAccountService.deleteByIds(collect);
            return ResponseResult.success("ok");
        }
        return ResponseResult.fail("deleteByIds fail");
    }

}
