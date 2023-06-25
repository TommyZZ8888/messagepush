package com.zz.messagepush.handler.handler.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.nacos.shaded.com.google.common.base.Throwables;
import com.dingtalk.api.DefaultDingTalkClient;
import com.dingtalk.api.request.OapiMessageCorpconversationAsyncsendV2Request;
import com.dingtalk.api.request.OapiMessageCorpconversationGetsendresultRequest;
import com.dingtalk.api.request.OapiMessageCorpconversationRecallRequest;
import com.dingtalk.api.response.OapiMessageCorpconversationAsyncsendV2Response;
import com.dingtalk.api.response.OapiMessageCorpconversationGetsendresultResponse;
import com.dingtalk.api.response.OapiMessageCorpconversationRecallResponse;
import com.taobao.api.ApiException;
import com.zz.messagepush.common.constant.AustinConstant;
import com.zz.messagepush.common.constant.SendAccountConstant;
import com.zz.messagepush.common.domain.LogParam;
import com.zz.messagepush.common.domain.dto.TaskInfo;
import com.zz.messagepush.common.domain.dto.account.DingDingWorkNoticeAccount;
import com.zz.messagepush.common.domain.dto.model.DingDingWorkContentModel;
import com.zz.messagepush.common.enums.ChannelType;
import com.zz.messagepush.common.enums.SendMessageType;
import com.zz.messagepush.common.utils.EnumUtil;
import com.zz.messagepush.handler.handler.BaseHandler;
import com.zz.messagepush.handler.handler.Handler;
import com.zz.messagepush.support.config.SupportThreadPoolConfig;
import com.zz.messagepush.support.domain.entity.MessageTemplateEntity;
import com.zz.messagepush.support.utils.AccountUtils;
import com.zz.messagepush.support.utils.LogUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * @Description
 * @Author 张卫刚
 * @Date Created on 2023/4/18
 */

@Slf4j
@Component
public class DingDingWorkNoticeHandler extends BaseHandler implements Handler {

    @Autowired
    private AccountUtils accountUtils;

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Autowired
    private LogUtils logUtils;

    private static final String SEND_URL = "https://oapi.dingtalk.com/topapi/message/corpconversation/asyncsend_v2";
    private static final String RECALL_URL = "https://oapi.dingtalk.com/topapi/message/corpconversation/recall";
    private static final String RECALL_BIZ_TYPE = "DingDingWorkNoticeHandler#recall";
    private static final String PULL_URL = "https://oapi.dingtalk.com/topapi/message/corpconversation/getsendresult";

    private static final String DING_DING_RECALL_KEY_PREFIX = "RECALL_";


    public DingDingWorkNoticeHandler() {
        channelCode = ChannelType.DING_DING_WORK_NOTICE.getCode();
    }

    @Override
    public boolean handler(TaskInfo taskInfo) {

        try {
            DingDingWorkNoticeAccount account = accountUtils.getAccountById(taskInfo.getSendAccount(), DingDingWorkNoticeAccount.class);
            OapiMessageCorpconversationAsyncsendV2Request request = assembleParam(account, taskInfo);
            String accessToken = redisTemplate.opsForValue().get(SendAccountConstant.DING_DING_ACCESS_TOKEN_PREFIX + taskInfo.getSendAccount());
            OapiMessageCorpconversationAsyncsendV2Response response = new DefaultDingTalkClient(SEND_URL).execute(request, accessToken);
            //发送成功后记录taskid,用于消息撤回（只支持当天）
            if (response.getErrcode() == 0) {
                redisTemplate.opsForList().leftPush(DING_DING_RECALL_KEY_PREFIX + taskInfo.getMessageTemplateId(), String.valueOf(response.getTaskId()));
                redisTemplate.expire(DING_DING_RECALL_KEY_PREFIX + taskInfo.getMessageTemplateId(), (DateUtil.endOfDay(new Date()).getTime() - DateUtil.current()) / 1000, TimeUnit.SECONDS);
                return true;
            }
        } catch (ApiException e) {
            log.error("DingDingWorkNoticeHandler#handler fail!{},params:{}", Throwables.getStackTraceAsString(e), taskInfo);
        }
        return false;
    }


    private OapiMessageCorpconversationAsyncsendV2Request assembleParam(DingDingWorkNoticeAccount account, TaskInfo taskInfo) {
        OapiMessageCorpconversationAsyncsendV2Request req = new OapiMessageCorpconversationAsyncsendV2Request();
        DingDingWorkContentModel contentModel = (DingDingWorkContentModel) taskInfo.getContentModel();

        try {
            if (AustinConstant.SEND_ALL.equals(CollUtil.getFirst(taskInfo.getReceiver()))) {
                req.setToAllUser(true);
            } else {
                req.setUseridList(Strings.join(taskInfo.getReceiver(), StrUtil.C_COMMA));
            }
            req.setAgentId(Long.parseLong(account.getAgentId()));

            //内容相关
            OapiMessageCorpconversationAsyncsendV2Request.Msg message = new OapiMessageCorpconversationAsyncsendV2Request.Msg();
            SendMessageType enumByCode = EnumUtil.getEnumByCode(SendMessageType.class, contentModel.getSendType());
            message.setMsgtype(enumByCode.getDingDingWorkType());

            // 根据类型设置入参
            if (SendMessageType.TEXT.getCode().equals(contentModel.getSendType())) {
                OapiMessageCorpconversationAsyncsendV2Request.Text textObj = new OapiMessageCorpconversationAsyncsendV2Request.Text();
                textObj.setContent(contentModel.getContent());
                message.setText(textObj);
            }
            if (SendMessageType.IMAGE.getCode().equals(contentModel.getSendType())) {
                OapiMessageCorpconversationAsyncsendV2Request.Image image = new OapiMessageCorpconversationAsyncsendV2Request.Image();
                image.setMediaId(contentModel.getMediaId());
                message.setImage(image);
            }
            if (SendMessageType.VOICE.getCode().equals(contentModel.getSendType())) {
                OapiMessageCorpconversationAsyncsendV2Request.Voice voice = new OapiMessageCorpconversationAsyncsendV2Request.Voice();
                voice.setMediaId(contentModel.getMediaId());
                voice.setDuration(contentModel.getDuration());
                message.setVoice(voice);
            }
            if (SendMessageType.FILE.getCode().equals(contentModel.getSendType())) {
                OapiMessageCorpconversationAsyncsendV2Request.File file = new OapiMessageCorpconversationAsyncsendV2Request.File();
                file.setMediaId(contentModel.getMediaId());
                message.setFile(file);
            }
            if (SendMessageType.LINK.getCode().equals(contentModel.getSendType())) {
                OapiMessageCorpconversationAsyncsendV2Request.Link link = new OapiMessageCorpconversationAsyncsendV2Request.Link();
                link.setText(contentModel.getContent());
                link.setTitle(contentModel.getTitle());
                link.setPicUrl(contentModel.getMediaId());
                link.setMessageUrl(contentModel.getUrl());
                message.setLink(link);
            }

            if (SendMessageType.MARKDOWN.getCode().equals(contentModel.getSendType())) {
                OapiMessageCorpconversationAsyncsendV2Request.Markdown markdown = new OapiMessageCorpconversationAsyncsendV2Request.Markdown();
                markdown.setText(contentModel.getContent());
                markdown.setTitle(contentModel.getTitle());
                message.setMarkdown(markdown);

            }
            if (SendMessageType.ACTION_CARD.getCode().equals(contentModel.getSendType())) {
                OapiMessageCorpconversationAsyncsendV2Request.ActionCard actionCard = new OapiMessageCorpconversationAsyncsendV2Request.ActionCard();
                actionCard.setTitle(contentModel.getTitle());
                actionCard.setMarkdown(contentModel.getContent());
                actionCard.setBtnOrientation(contentModel.getBtnOrientation());
                actionCard.setBtnJsonList(JSON.parseArray(contentModel.getBtns(), OapiMessageCorpconversationAsyncsendV2Request.BtnJsonList.class));
                message.setActionCard(actionCard);
            }
            if (SendMessageType.OA.getCode().equals(contentModel.getSendType())) {
                OapiMessageCorpconversationAsyncsendV2Request.OA oa = new OapiMessageCorpconversationAsyncsendV2Request.OA();
                oa.setMessageUrl(contentModel.getUrl());
                oa.setHead(JSON.parseObject(contentModel.getDingDingOaHead(), OapiMessageCorpconversationAsyncsendV2Request.Head.class));
                oa.setBody(JSON.parseObject(contentModel.getDingDingOaBody(), OapiMessageCorpconversationAsyncsendV2Request.Body.class));
                message.setOa(oa);
            }
            req.setMsg(message);
        } catch (NumberFormatException e) {
            log.error("DingDingWorkNoticeHandler#assembleParam fail: {}", Throwables.getStackTraceAsString(e));
        }
        return req;
    }

    @Override
    public void recall(MessageTemplateEntity messageTemplate) {
        SupportThreadPoolConfig.getPendingSingleThreadPool().execute(() -> {
            try {
                DingDingWorkNoticeAccount account = accountUtils.getAccountById(messageTemplate.getSendAccount(), DingDingWorkNoticeAccount.class);
                String accessToken = redisTemplate.opsForValue().get(SendAccountConstant.DING_DING_ACCESS_TOKEN_PREFIX + messageTemplate.getSendAccount());
                Long size = redisTemplate.opsForList().size(DING_DING_RECALL_KEY_PREFIX + messageTemplate.getId());
                if (size == null) {
                    return;
                }
                while (0 < Objects.requireNonNull(redisTemplate.opsForList().size(DING_DING_RECALL_KEY_PREFIX + messageTemplate.getId()))) {
                    String taskId = redisTemplate.opsForList().leftPop(DING_DING_RECALL_KEY_PREFIX + messageTemplate.getId());
                    DefaultDingTalkClient client = new DefaultDingTalkClient(RECALL_URL);
                    OapiMessageCorpconversationRecallRequest request = new OapiMessageCorpconversationRecallRequest();
                    request.setAgentId(Long.valueOf(account.getAgentId()));
                    assert taskId != null;
                    request.setMsgTaskId(Long.valueOf(taskId));
                    OapiMessageCorpconversationRecallResponse response = client.execute(request, accessToken);
                    logUtils.print(LogParam.builder().bizType(RECALL_BIZ_TYPE).object(JSON.toJSONString(response)).build());
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public void pull(Long accountId) {
        try {
            DingDingWorkNoticeAccount account = accountUtils.getAccountById(accountId.intValue(), DingDingWorkNoticeAccount.class);
            String accessToken = redisTemplate.opsForValue().get(SendAccountConstant.DING_DING_ACCESS_TOKEN_PREFIX + accountId);
            OapiMessageCorpconversationGetsendresultRequest request = new OapiMessageCorpconversationGetsendresultRequest();
            DefaultDingTalkClient client = new DefaultDingTalkClient(PULL_URL);
            request.setAgentId(Long.valueOf(account.getAgentId()));
            request.setTaskId(456L);
            OapiMessageCorpconversationGetsendresultResponse execute = client.execute(request, accessToken);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
