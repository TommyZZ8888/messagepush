package com.zz.messagepush.web.controller;

import cn.hutool.core.util.StrUtil;
import com.alibaba.nacos.common.tls.SelfTrustManager;
import com.zz.messagepush.common.constant.AustinConstant;
import com.zz.messagepush.common.domain.ResponseResult;
import com.zz.messagepush.common.enums.RespStatusEnum;
import com.zz.messagepush.support.domain.entity.ChannelAccountEntity;
import com.zz.messagepush.web.domain.vo.amis.CommonAmisVo;
import com.zz.messagepush.web.service.ChannelAccountService;
import com.zz.messagepush.web.utils.Convert4Amis;
import com.zz.messagepush.web.utils.LoginUtil;
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
public class ChannelAccountController {


    @Autowired
    private ChannelAccountService channelAccountService;

    @Autowired
    private LoginUtil loginUtil;


    @ApiOperation("保存数据")
    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public ResponseResult<Boolean> saveOrUpdate(@RequestBody ChannelAccountEntity channelAccountEntity) {
        if (loginUtil.needLogin() && StrUtil.isBlank(channelAccountEntity.getCreator())) {
            return ResponseResult.fail(RespStatusEnum.NO_LOGIN.getDescription());
        }
        channelAccountEntity.setCreator(StrUtil.isBlank(channelAccountEntity.getCreator()) ? AustinConstant.DEFAULT_CREATOR : channelAccountEntity.getCreator());
        return ResponseResult.success("success");
    }


    @ApiOperation("查询数据")
    @RequestMapping(value = "/query", method = RequestMethod.POST)
    public ResponseResult<List<CommonAmisVo>> query(Integer channelType, String creator) {
        channelAccountService.queryByChannelType(channelType,creator);
        creator = StrUtil.isBlank(creator) ? AustinConstant.DEFAULT_CREATOR : creator;

        List<ChannelAccountEntity> channelAccounts = channelAccountService.queryByChannelType(channelType, creator);
        return ResponseResult.success("ok",Convert4Amis.getChannelAccountVo(channelAccounts));
    }

    @ApiOperation("渠道账户列表信息")
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public ResponseResult<List<ChannelAccountEntity>> list(String creator) {
            if (loginUtil.needLogin() && StrUtil.isBlank(creator)) {
                return ResponseResult.fail(RespStatusEnum.NO_LOGIN.getDescription());
            }
            creator = StrUtil.isBlank(creator) ? AustinConstant.DEFAULT_CREATOR : creator;

        return ResponseResult.success("ok", channelAccountService.list(creator));
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
