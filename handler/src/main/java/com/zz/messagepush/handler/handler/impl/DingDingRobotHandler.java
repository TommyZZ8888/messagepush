package com.zz.messagepush.handler.handler.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.nacos.shaded.com.google.common.base.Throwables;
import com.twitter.chill.Base64;
import com.zz.messagepush.common.constant.AustinConstant;
import com.zz.messagepush.common.constant.CommonConstant;
import com.zz.messagepush.common.domain.dto.model.DingDingRobotContentModel;
import com.zz.messagepush.common.domain.dto.TaskInfo;
import com.zz.messagepush.common.enums.ChannelType;
import com.zz.messagepush.common.domain.dto.account.DingDingRobotAccount;
import com.zz.messagepush.common.enums.SendMessageType;
import com.zz.messagepush.common.utils.EnumUtil;
import com.zz.messagepush.handler.domain.dingding.DingDingRobotResult;
import com.zz.messagepush.handler.domain.dingding.DingDingRobotParam;
import com.zz.messagepush.handler.handler.BaseHandler;
import com.zz.messagepush.handler.handler.Handler;
import com.zz.messagepush.support.domain.entity.MessageTemplateEntity;
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
import java.util.List;

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

    @Autowired
    private AccountUtils accountUtils;

    @Override
    public boolean handler(TaskInfo taskInfo) {
        DingDingRobotAccount account = accountUtils.getAccountById(taskInfo.getSendAccount(), DingDingRobotAccount.class);
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

        DingDingRobotContentModel contentModel = (DingDingRobotContentModel) taskInfo.getContentModel();
        SendMessageType enumByCode = EnumUtil.getEnumByCode(SendMessageType.class, contentModel.getSendType());
        DingDingRobotParam param = DingDingRobotParam.builder().at(atVO).msgType(enumByCode.getDingDingRobotType()).build();
        if (SendMessageType.TEXT.getCode().equals(contentModel.getSendType())){
            param.setText(DingDingRobotParam.TextVO.builder().content(contentModel.getContent()).build());
        }
        if (SendMessageType.MARKDOWN.getCode().equals(contentModel.getSendType())){
            param.setMarkdown(DingDingRobotParam.MarkdownVO.builder().title(contentModel.getTitle()).text(contentModel.getContent()).build());
        }
        if (SendMessageType.LINK.getCode().equals(contentModel.getSendType())) {
            param.setLink(DingDingRobotParam.LinkVO.builder().title(contentModel.getTitle()).text(contentModel.getContent()).messageUrl(contentModel.getUrl()).picUrl(contentModel.getPicUrl()).build());
        }
        if (SendMessageType.NEWS.getCode().equals(contentModel.getSendType())) {
            List<DingDingRobotParam.FeedCardVO.LinksVO> linksVOS = JSON.parseArray(contentModel.getFeedCards(), DingDingRobotParam.FeedCardVO.LinksVO.class);
            DingDingRobotParam.FeedCardVO feedCardVO = DingDingRobotParam.FeedCardVO.builder().links(linksVOS).build();
            param.setFeedCard(feedCardVO);
        }
        if (SendMessageType.ACTION_CARD.getCode().equals(contentModel.getSendType())) {
            List<DingDingRobotParam.ActionCardVO.BtnsVO> btnsVOS = JSON.parseArray(contentModel.getBtns(), DingDingRobotParam.ActionCardVO.BtnsVO.class);
            DingDingRobotParam.ActionCardVO actionCardVO = DingDingRobotParam.ActionCardVO.builder().title(contentModel.getTitle()).text(contentModel.getContent()).btnOrientation(contentModel.getBtnOrientation()).btns(btnsVOS).build();
            param.setActionCard(actionCardVO);
        }

        return param;
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
            Mac mac = Mac.getInstance(CommonConstant.HMAC_SHA256_ENCRYPTION_ALGO);
            mac.init(new SecretKeySpec(secret.getBytes(CommonConstant.CHARSET_NAME), CommonConstant.HMAC_SHA256_ENCRYPTION_ALGO));
            byte[] signDatas = mac.doFinal(stringToSign.getBytes(CommonConstant.CHARSET_NAME));
            sign = URLEncoder.encode(Base64.encodeBytes(signDatas), CommonConstant.CHARSET_NAME);
        } catch (NoSuchAlgorithmException | InvalidKeyException | UnsupportedEncodingException e) {
            log.error("dingDingRobotHandler#assemblySign fail: {}", Throwables.getStackTraceAsString(e));
        }
        return sign;
    }

    @Override
    public void recall(MessageTemplateEntity messageTemplate) {

    }
}
