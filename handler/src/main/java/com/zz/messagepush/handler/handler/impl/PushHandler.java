package com.zz.messagepush.handler.handler.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.http.ContentType;
import cn.hutool.http.Header;
import cn.hutool.http.HttpException;
import cn.hutool.http.HttpRequest;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.nacos.shaded.com.google.common.base.Throwables;
import com.zz.messagepush.common.constant.SendAccountConstant;
import com.zz.messagepush.common.domain.dto.TaskInfo;
import com.zz.messagepush.common.domain.dto.account.GeTuiAccount;
import com.zz.messagepush.common.domain.dto.model.PushContentModel;
import com.zz.messagepush.common.enums.ChannelType;
import com.zz.messagepush.handler.domain.push.PushParam;
import com.zz.messagepush.handler.domain.push.getui.BatchSendPushParam;
import com.zz.messagepush.handler.domain.push.getui.SendPushParam;
import com.zz.messagepush.handler.domain.push.getui.SendPushResult;
import com.zz.messagepush.handler.handler.BaseHandler;
import com.zz.messagepush.handler.handler.Handler;
import com.zz.messagepush.support.domain.entity.MessageTemplateEntity;
import com.zz.messagepush.support.utils.AccountUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.Set;

/**
 * @Description 通知栏消息发送处理
 * @Author 张卫刚
 * @Date Created on 2023/4/25
 */

@Component
@Slf4j
public class PushHandler extends BaseHandler implements Handler {

    private final static String BASE_URL = "https://restapi.getui.com/v2/";
    private final static String SINGLE_PUSH_PATH = "/push/single/cid";
    private final static String BATCH_PUSH_CREATE_TASK_PATH = "/push/list/message";
    private final static String BATCH_PUSH_PATH = "/push/list/cid";


    public PushHandler() {
        channelCode = ChannelType.PUSH.getCode();
    }

    @Autowired
    private AccountUtils accountUtils;

    @Autowired
    private StringRedisTemplate redisTemplate;


    @Override
    public boolean handler(TaskInfo taskInfo) {
        try {
            GeTuiAccount account = accountUtils.getAccountById(taskInfo.getSendAccount(), GeTuiAccount.class);
            String token = redisTemplate.opsForValue().get(SendAccountConstant.GE_TUI_ACCESS_TOKEN_PREFIX + taskInfo.getSendAccount());
            PushParam pushParam = PushParam.builder().token(token).taskInfo(taskInfo).appId(account.getAppId()).build();
            String result;
            if (taskInfo.getReceiver().size() == 1) {
                result = singlePush(pushParam);
            } else {
                String taskId = createTaskId(pushParam);
                result = batchPush(taskId, pushParam);
            }
            SendPushResult sendPushResult = JSONObject.parseObject(result, SendPushResult.class);
            if (sendPushResult.getCode().equals(0)) {
                return true;
            }
        } catch (Exception e) {
            log.error("PushHandler#handler fail!e:{},params:{}", Throwables.getStackTraceAsString(e), JSON.toJSONString(taskInfo));
        }
        return false;
    }


    /**
     * 单推
     *
     * @param pushParam
     * @return
     */
    private String singlePush(PushParam pushParam) {
        String url = BASE_URL + pushParam.getAppId() + SINGLE_PUSH_PATH;
        SendPushParam sendPushParam = assembleParam((PushContentModel) pushParam.getTaskInfo().getContentModel(), pushParam.getTaskInfo().getReceiver());
        return HttpRequest.post(url).header("token", pushParam.getToken())
                .header(Header.CONTENT_TYPE.getValue(), ContentType.JSON.getValue())
                .body(JSON.toJSONString(sendPushParam))
                .timeout(2000)
                .execute().body();
    }

    private String batchPush(String taskId, PushParam pushParam) {
        String url = BASE_URL + pushParam.getAppId() + BATCH_PUSH_PATH;
        BatchSendPushParam batchSendPushParam = BatchSendPushParam.builder()
                .taskId(taskId)
                .isAsync(true)
                .audience(BatchSendPushParam.AudienceVO.builder().cid(pushParam.getTaskInfo().getReceiver()).build()).build();
        return HttpRequest.post(url).header("token", pushParam.getToken())
                .header(Header.CONTENT_TYPE.getValue(), ContentType.JSON.getValue())
                .body(JSON.toJSONString(batchSendPushParam))
                .timeout(2000)
                .execute().body();
    }

    private String createTaskId(PushParam pushParam) {
        String url = BASE_URL + pushParam.getAppId() + BATCH_PUSH_CREATE_TASK_PATH;
        SendPushParam sendPushParam = assembleParam((PushContentModel) pushParam.getTaskInfo().getContentModel(), pushParam.getTaskInfo().getReceiver());
        String taskId = "";
        try {
            String body = HttpRequest.post(url).header("token", pushParam.getToken())
                    .header(Header.CONTENT_TYPE.getValue(), ContentType.JSON.getValue())
                    .body(JSON.toJSONString(sendPushParam))
                    .timeout(2000)
                    .execute().body();
            SendPushResult sendPushResult = JSONObject.parseObject(body, SendPushResult.class);
            taskId = sendPushResult.getData().getString("taskId");
        } catch (HttpException e) {
            log.error("pushHandler#createTaskId fail:{}", Throwables.getStackTraceAsString(e));
        }
        return taskId;
    }

    private SendPushParam assembleParam(PushContentModel pushContentModel) {
        return assembleParam(pushContentModel, null);
    }

    private SendPushParam assembleParam(PushContentModel pushContentModel, Set<String> cid) {
        SendPushParam.PushMessageVO.NotificationVO notificationVO = SendPushParam.PushMessageVO.NotificationVO.builder().body(pushContentModel.getContent())
                .title(pushContentModel.getTitle()).clickType("startapp").build();
        SendPushParam sendPushParam = SendPushParam.builder().requestId(String.valueOf(IdUtil.getSnowflake().nextId())).pushMessage(SendPushParam.PushMessageVO.builder().notification(notificationVO).build()).build();

        if (CollUtil.isNotEmpty(cid)) {
            sendPushParam.setAudience(SendPushParam.AudienceVO.builder().cid(cid).build());
        }
        return sendPushParam;
    }

    @Override
    public void recall(MessageTemplateEntity messageTemplate) {

    }
}
