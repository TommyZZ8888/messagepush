package com.zz.messagepush.web.controller;

import com.zz.messagepush.common.domain.ResponseResult;
import com.zz.messagepush.common.enums.ChannelType;
import com.zz.messagepush.handler.handler.impl.RefreshDingDingAccessTokenHandler;
import com.zz.messagepush.handler.handler.impl.RefreshGeTuiTokenHandler;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Description RefreshTokenController
 * @Author 张卫刚
 * @Date Created on 2023/4/26
 */

@RestController
@RequestMapping("/refresh")
public class RefreshTokenController {

    @Autowired
    private RefreshGeTuiTokenHandler refreshGeTuiTokenHandler;

    @Autowired
    private RefreshDingDingAccessTokenHandler refreshDingDingAccessTokenHandler;


    @ApiOperation("刷新token")
    @RequestMapping(value = "/geTuiToken", method = RequestMethod.POST)
    public ResponseResult<Boolean> refresh(Integer channelCode) {
        if (channelCode.equals(ChannelType.DING_DING_WORK_NOTICE.getCode())) {
            refreshDingDingAccessTokenHandler.execute();
        }
        if (channelCode.equals(ChannelType.PUSH.getCode())) {
            refreshGeTuiTokenHandler.execute();
        }
        return ResponseResult.success();
    }
}
