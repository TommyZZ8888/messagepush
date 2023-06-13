package com.zz.messagepush.handler.handler.impl;

import cn.hutool.http.ContentType;
import cn.hutool.http.Header;
import cn.hutool.http.HttpException;
import cn.hutool.http.HttpRequest;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.nacos.shaded.com.google.common.base.Throwables;
import com.zz.messagepush.common.constant.SendAccountConstant;
import com.zz.messagepush.common.domain.dto.TaskInfo;
import com.zz.messagepush.common.domain.dto.account.FeiShuRobotAccount;
import com.zz.messagepush.common.domain.dto.model.FeiShuRobotContentModel;
import com.zz.messagepush.common.enums.ChannelType;
import com.zz.messagepush.common.enums.SendMessageType;
import com.zz.messagepush.handler.domain.feishu.FeiShuRobotParam;
import com.zz.messagepush.handler.domain.feishu.FeiShuRobotResult;
import com.zz.messagepush.handler.handler.BaseHandler;
import com.zz.messagepush.handler.handler.Handler;
import com.zz.messagepush.support.domain.entity.MessageTemplateEntity;
import com.zz.messagepush.support.utils.AccountUtils;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.internal.bytebuddy.implementation.bytecode.Throw;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @Description FeiShuRobotHandler
 * @Author 张卫刚
 * @Date Created on 2023/5/16
 */

@Service
@Slf4j
public class FeiShuRobotHandler extends BaseHandler implements Handler {

    @Autowired
    private AccountUtils accountUtils;

    public FeiShuRobotHandler() {
        channelCode = ChannelType.FEI_SHU_ROBOT.getCode();
    }


    @Override
    public boolean handler(TaskInfo taskInfo) {
        try {
            FeiShuRobotAccount account = accountUtils.getAccountById(taskInfo.getSendAccount(), FeiShuRobotAccount.class);
            FeiShuRobotParam feiShuRobotParam = assembleParam(taskInfo);
            String body = HttpRequest.post(account.getWebhook())
                    .header(Header.CONTENT_TYPE.getValue(), ContentType.JSON.getValue())
                    .body(JSON.toJSONString(feiShuRobotParam))
                    .execute().body();
            FeiShuRobotResult feiShuRobotResult = JSONObject.parseObject(body, FeiShuRobotResult.class);
            if (feiShuRobotResult.getStatusCode() == 0) {
                return true;
            }
        } catch (HttpException e) {
            log.error("feiShuRobotHandler#handler fail: {},param: {}", Throwables.getStackTraceAsString(e), JSON.toJSONString(taskInfo));
        }
        return false;
    }

    private FeiShuRobotParam assembleParam(TaskInfo taskInfo) {
        FeiShuRobotContentModel contentModel = (FeiShuRobotContentModel) taskInfo.getContentModel();
        FeiShuRobotParam param = FeiShuRobotParam.builder().msgType(SendMessageType.getFeiShuRobotTypeByCode(contentModel.getSendType())).build();

        if (SendMessageType.TEXT.getCode().equals(contentModel.getSendType())) {
            FeiShuRobotParam.ContentDTO contentDTO = FeiShuRobotParam.ContentDTO.builder().text(contentModel.getContent()).build();
            param.setContent(contentDTO);
        }
        if (SendMessageType.RICH_TEXT.getCode().equals(contentModel.getSendType())) {
            List<FeiShuRobotParam.ContentDTO.PostDTO.ZhCnDTO.PostContentDTO> postContentDTOS = JSON.parseArray(contentModel.getPostContent(), FeiShuRobotParam.ContentDTO.PostDTO.ZhCnDTO.PostContentDTO.class);
            List<List<FeiShuRobotParam.ContentDTO.PostDTO.ZhCnDTO.PostContentDTO>> postContentList = new ArrayList<>();
            postContentList.add(postContentDTOS);
            FeiShuRobotParam.ContentDTO.PostDTO postDTO = FeiShuRobotParam.ContentDTO.PostDTO.builder()
                    .zhCn(FeiShuRobotParam.ContentDTO.PostDTO.ZhCnDTO.builder().title(contentModel.getTitle()).content(postContentList).build())
                    .build();
            param.setContent(FeiShuRobotParam.ContentDTO.builder().post(postDTO).build());
        }
        if (SendMessageType.SHARE_CHAT.getCode().equals(contentModel.getSendType())) {
            param.setContent(FeiShuRobotParam.ContentDTO.builder().shareChatId(contentModel.getMediaId()).build());
        }
        if (SendMessageType.IMAGE.getCode().equals(contentModel.getSendType())) {
            param.setContent(FeiShuRobotParam.ContentDTO.builder().imageKey(contentModel.getMediaId()).build());
        }
        if (SendMessageType.ACTION_CARD.getCode().equals(contentModel.getSendType())) {
            //
        }
        return param;
    }

    @Override
    public void recall(MessageTemplateEntity messageTemplate) {

    }
}
