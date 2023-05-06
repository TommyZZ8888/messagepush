package com.zz.messagepush.service.api.impl.action;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ReUtil;
import com.zz.messagepush.common.domain.ResponseResult;
import com.zz.messagepush.common.domain.dto.TaskInfo;
import com.zz.messagepush.common.enums.IdType;
import com.zz.messagepush.common.enums.RespStatusEnum;
import com.zz.messagepush.service.api.impl.domain.SendTaskModel;
import com.zz.messagepush.support.pipeline.BusinessProcess;
import com.zz.messagepush.support.pipeline.ProcessContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @Description
 * @Author 张卫刚
 * @Date Created on 2023/3/21
 */
@Slf4j
@Service
public class AfterParamCheckAction implements BusinessProcess<SendTaskModel> {

    public static final String EMAIL_REGEX_EXP = "^[A-Za-z0-9-_\\u4e00-\\u9fa5]+@[a-zA-Z0-9_-]+(\\.[a-zA-Z0-9_-]+)+$";
    public static final String PHONE_REGEX_EXP = "^((13[0-9])|(14[5,7,9])|(15[0-3,5-9])|(166)|(17[0-9])|(18[0-9])|(19[1,8,9]))\\d{8}$";

    private static final Map<Integer, String> CHANNEL_REGEX_EXP = new HashMap<>();

    static {
        CHANNEL_REGEX_EXP.put(IdType.PHONE.getCode(), PHONE_REGEX_EXP);
        CHANNEL_REGEX_EXP.put(IdType.EMAIL.getCode(), EMAIL_REGEX_EXP);
    }

    @Override
    public void process(ProcessContext<SendTaskModel> context) {
        SendTaskModel processModel = context.getProcessModel();
        List<TaskInfo> taskInfo = processModel.getTaskInfo();

        /**
         * 过滤掉不合法的手机号、邮件
         */
        filterIllegalReceiver(taskInfo);

        if (CollUtil.isEmpty(taskInfo)) {
            context.setNeedBreak(true).setResponse(ResponseResult.fail(RespStatusEnum.CLIENT_BAD_PARAMETERS.getDescription()));
        }
    }


    /**
     * 如果指定类型是手机号，且渠道是手机短信，则检测输入手机号是否合理
     *
     * @param taskInfoList
     */
    private void filterIllegalReceiver(List<TaskInfo> taskInfoList) {
        Integer idType = taskInfoList.get(0).getIdType();
        filter(taskInfoList, CHANNEL_REGEX_EXP.get(idType));
    }

    /**
     * 利用正则过滤掉不合法的接收者
     *
     * @param taskInfoList
     * @param regexExp
     */
    private void filter(List<TaskInfo> taskInfoList, String regexExp) {
        Iterator<TaskInfo> iterator = taskInfoList.iterator();
        while (iterator.hasNext()) {
            TaskInfo taskInfo = iterator.next();
            Set<String> illegalPhone = taskInfo.getReceiver().stream().filter(phone -> ReUtil.isMatch(regexExp, phone)).collect(Collectors.toSet());
            if (CollUtil.isNotEmpty(illegalPhone)) {
                taskInfo.getReceiver().removeAll(illegalPhone);
            }
            if (CollUtil.isEmpty(taskInfo.getReceiver())) {
                iterator.remove();
            }
        }
    }
}
