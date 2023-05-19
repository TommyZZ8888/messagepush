package com.zz.messagepush.handler.receiver.service;

import cn.hutool.core.collection.CollUtil;
import com.zz.messagepush.common.domain.AnchorInfo;
import com.zz.messagepush.common.domain.LogParam;
import com.zz.messagepush.common.domain.dto.TaskInfo;
import com.zz.messagepush.common.enums.AnchorStateEnum;
import com.zz.messagepush.common.utils.ApplicationContextUtil;
import com.zz.messagepush.handler.handler.HandlerHolder;
import com.zz.messagepush.handler.pending.Task;
import com.zz.messagepush.handler.pending.TaskPendingHolder;
import com.zz.messagepush.handler.utils.GroupIdMappingUtils;
import com.zz.messagepush.support.domain.dto.MessageTemplateParamDTO;
import com.zz.messagepush.support.domain.entity.MessageTemplateEntity;
import com.zz.messagepush.support.utils.LogUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Description ConsumeServiceImpl
 * @Author 张卫刚
 * @Date Created on 2023/5/17
 */

@Service
public class ConsumeServiceImpl implements ConsumeService {

    private static final String LOG_BIZ_TYPE = "Receiver#send";
    private static final String LOG_BIZ_RECALL_TYPE = "Receiver#recall";

    @Autowired
    private TaskPendingHolder taskPendingHolder;

    @Autowired
    private HandlerHolder handlerHolder;

    @Autowired
    private LogUtils logUtils;

    @Override
    public void consume2Send(List<TaskInfo> list) {
        String topicGroupId = GroupIdMappingUtils.getGroupIdByTaskInfo(CollUtil.getFirst(list));
        for (TaskInfo taskInfo : list) {
            logUtils.print(LogParam.builder().bizType(LOG_BIZ_TYPE).object(taskInfo).build(),
                    AnchorInfo.builder().businessId(taskInfo.getBusinessId()).ids(taskInfo.getReceiver()).state(AnchorStateEnum.RECEIVE.getCode()).build());
            Task task = ApplicationContextUtil.getBean(Task.class).setTaskInfo(taskInfo);
            taskPendingHolder.route(topicGroupId).execute(task);
        }
    }

    @Override
    public void consume2Recall(MessageTemplateEntity messageTemplateEntity) {
        logUtils.print(LogParam.builder().bizType(LOG_BIZ_RECALL_TYPE).object(messageTemplateEntity).build());
        handlerHolder.route(messageTemplateEntity.getSendChannel()).recall(messageTemplateEntity);
    }
}
