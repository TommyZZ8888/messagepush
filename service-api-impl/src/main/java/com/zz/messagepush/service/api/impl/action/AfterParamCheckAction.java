package com.zz.messagepush.service.api.impl.action;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ReUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.tencentcloudapi.tcaplusdb.v20190823.models.IdlFileInfo;
import com.zz.messagepush.common.domain.ResponseResult;
import com.zz.messagepush.common.domain.dto.TaskInfo;
import com.zz.messagepush.common.enums.ChannelType;
import com.zz.messagepush.common.enums.IdType;
import com.zz.messagepush.common.enums.RespStatusEnum;
import com.zz.messagepush.service.api.impl.domain.SendTaskModel;
import com.zz.messagepush.support.pipeline.BusinessProcess;
import com.zz.messagepush.support.pipeline.ProcessContext;
import lombok.extern.slf4j.Slf4j;

import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @Description
 * @Author 张卫刚
 * @Date Created on 2023/3/21
 */
@Slf4j
public class AfterParamCheckAction implements BusinessProcess {

    public static final String PHONE_REGEX_EXP = "^((13[0-9])|(14[5,7,9])|(15[0-3,5-9])|(166)|(17[0-9])|(18[0-9])|(19[1,8,9]))\\d{8}$";

    @Override
    public void process(ProcessContext context) {
        SendTaskModel processModel = (SendTaskModel) context.getProcessModel();
        List<TaskInfo> taskInfo = processModel.getTaskInfo();

        filterIllegalPhoneNum(taskInfo);

        if (CollUtil.isEmpty(taskInfo)) {
            context.setNeedBreak(true).setResponse(ResponseResult.fail(RespStatusEnum.CLIENT_BAD_PARAMETERS.getDescription()));
        }
    }


    /**
     * 如果指定类型是手机号，且渠道是手机短信，则检测输入手机号是否合理
     *
     * @param taskInfoList
     */
    private void filterIllegalPhoneNum(List<TaskInfo> taskInfoList) {
        Integer idType = taskInfoList.get(0).getIdType();
        Integer sendChannel = taskInfoList.get(0).getSendChannel();

        if (IdType.PHONE.getCode().equals(idType) && ChannelType.SMS.getCode().equals(sendChannel)) {
            Iterator<TaskInfo> iterator = taskInfoList.iterator();

            if (iterator.hasNext()) {
                TaskInfo task = iterator.next();
                Set<String> illegalPhone = task.getReceiver().stream().filter(phone -> !ReUtil.isMatch(PHONE_REGEX_EXP, phone)).collect(Collectors.toSet());

                if (CollUtil.isNotEmpty(illegalPhone)) {
                    task.getReceiver().removeAll(illegalPhone);
                    log.error("{} find illegal phone!{}", task.getMessageTemplateId(), JSON.toJSONString(illegalPhone));
                }
                if (CollUtil.isEmpty(task.getReceiver())) {
                    iterator.remove();
                }
            }
        }
    }
}
