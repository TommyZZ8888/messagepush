package com.zz.messagepush.handler.handler.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.nacos.shaded.com.google.common.base.Throwables;
import com.twitter.chill.Base64;
import com.zz.messagepush.common.constant.AustinConstant;
import com.zz.messagepush.common.domain.dto.model.DingDingContentModel;
import com.zz.messagepush.common.domain.dto.TaskInfo;
import com.zz.messagepush.common.enums.ChannelType;
import com.zz.messagepush.common.domain.dto.account.DingDingRobotAccount;
import com.zz.messagepush.handler.domain.dingding.DingDingRobotResult;
import com.zz.messagepush.handler.domain.dto.DingDingRobotParam;
import com.zz.messagepush.handler.handler.BaseHandler;
import com.zz.messagepush.handler.handler.Handler;
import com.zz.messagepush.support.utils.AccountUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;

/**
 * @Description
 * @Author 张卫刚
 * @Date Created on 2023/4/17
 */
@Component
@Slf4j
public class DingDingRobotHandler extends BaseHandler implements Handler {

    public DingDingRobotHandler() {
        channelCode = ChannelType.DING_DING_ROBOT.getCode();
    }

    private static final String DING_DING_ROBOT_ACCOUNT_KEY = "dingDingRobotAccount";

    private static final String PREFIX = "ding_ding_robot_";

    @Autowired
    private AccountUtils accountUtils;

    @Override
    public boolean handler(TaskInfo taskInfo) {
        DingDingRobotAccount account = accountUtils.getAccount(taskInfo.getSendAccount(), DING_DING_ROBOT_ACCOUNT_KEY, PREFIX, new DingDingRobotAccount());
        DingDingRobotParam dingRobotParam = assemblyParam(taskInfo);
        String post = HttpUtil.post(assemblyParamUrl(account), JSONObject.toJSONString(dingRobotParam));
        DingDingRobotResult dingDingRobotResult = JSON.parseObject(post, DingDingRobotResult.class);
        if (dingDingRobotResult.getErrCode() == 0) {
            return true;
        } else {
            log.error("DingDingHandler#handler fail!result:{},params:{}", JSON.toJSONString(dingDingRobotResult), JSON.toJSONString(taskInfo));
        }
        return false;
    }

    /**
     * 组装参数
     *
     * @param taskInfo
     * @return
     */
    private DingDingRobotParam assemblyParam(TaskInfo taskInfo) {
        DingDingRobotParam.AtVO atVO = DingDingRobotParam.AtVO.builder().build();
        if (CollUtil.getFirst(taskInfo.getReceiver()).equals(AustinConstant.SEND_ALL)) {
            atVO.setIsAtAll(true);
        } else {
            atVO.setAtUserIds(new ArrayList<>(taskInfo.getReceiver()));
        }

        DingDingContentModel contentModel = (DingDingContentModel) taskInfo.getContentModel();
        return DingDingRobotParam.builder().at(atVO).msgType("text")
                .text(DingDingRobotParam.TextVO.builder().content(contentModel.getContent()).build()).build();
    }

    /**
     * 拼装url
     *
     * @param account
     * @return
     */
    private String assemblyParamUrl(DingDingRobotAccount account) {
        long currentTimeMillis = System.currentTimeMillis();
        String sign = assemblySign(currentTimeMillis, account.getSecret());
        return account.getWebhook() + "&timeStamp=" + currentTimeMillis + "&sign=" + sign;
    }

    /**
     * 使用HmacSHA256
     *
     * @param currentTimeMiles
     * @param secret
     * @return
     */
    private String assemblySign(long currentTimeMiles, String secret) {

        String sign = "";
        String stringToSign = currentTimeMiles + String.valueOf(StrUtil.C_LF) + secret;
        try {
            Mac mac = Mac.getInstance(AustinConstant.HMAC_SHA256_ENCRYPTION_ALGO);
            mac.init(new SecretKeySpec(secret.getBytes(AustinConstant.CHARSET_NAME), AustinConstant.HMAC_SHA256_ENCRYPTION_ALGO));
            byte[] signDatas = mac.doFinal(stringToSign.getBytes(AustinConstant.CHARSET_NAME));
            sign = URLEncoder.encode(Base64.encodeBytes(signDatas), AustinConstant.CHARSET_NAME);
        } catch (NoSuchAlgorithmException | InvalidKeyException | UnsupportedEncodingException e) {
            log.error("dingDingRobotHandler#assemblySign fail: {}", Throwables.getStackTraceAsString(e));
        }
        return sign;
    }
}
