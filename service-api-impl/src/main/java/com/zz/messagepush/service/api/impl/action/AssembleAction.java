package com.zz.messagepush.service.api.impl.action;

import cn.hutool.core.util.ReflectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.aliyun.teautil.Common;
import com.zz.messagepush.common.constant.AustinConstant;
import com.zz.messagepush.common.constant.CommonConstant;
import com.zz.messagepush.common.domain.ResponseResult;
import com.zz.messagepush.common.domain.dto.model.ContentModel;
import com.zz.messagepush.common.domain.dto.TaskInfo;
import com.zz.messagepush.common.enums.ChannelType;
import com.zz.messagepush.common.enums.RespStatusEnum;
import com.zz.messagepush.service.api.domain.dto.MessageParam;
import com.zz.messagepush.service.api.enums.BusinessCode;
import com.zz.messagepush.service.api.impl.domain.SendTaskModel;
import com.zz.messagepush.support.domain.entity.MessageTemplateEntity;
import com.zz.messagepush.support.mapper.MessageTemplateMapper;
import com.zz.messagepush.support.pipeline.BusinessProcess;
import com.zz.messagepush.support.pipeline.ProcessContext;
import com.zz.messagepush.support.utils.ContentHolderUtil;
import com.zz.messagepush.support.utils.TaskInfoUtils;
import org.apache.kafka.common.network.Send;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.reflect.Field;
import java.util.*;

/**
 * @Description 拼接参数
 * @Author 张卫刚
 * @Date Created on 2023/3/16
 */

@Service
public class AssembleAction implements BusinessProcess<SendTaskModel> {

    private final static String LINK_NAME = "url";

    @Autowired
    private MessageTemplateMapper messageTemplateMapper;

    @Override
    public void process(ProcessContext<SendTaskModel> context) {
        SendTaskModel processModel =  context.getProcessModel();
        Long messageTemplateId = processModel.getMessageTemplateId();

        MessageTemplateEntity messageTemplateEntity = messageTemplateMapper.findById(messageTemplateId).orElse(null);
        if (Objects.isNull(messageTemplateEntity) || messageTemplateEntity.getIsDeleted().equals(CommonConstant.TRUE)) {
            context.setNeedBreak(true).setResponse(ResponseResult.fail(RespStatusEnum.TEMPLATE_NOT_FOUND.getDescription()));
            return;
        }
        if (BusinessCode.COMMON_SEND.getCode().equals(context.getCode())){
            List<TaskInfo> taskInfos = assembleTaskInfo(processModel, messageTemplateEntity);
            processModel.setTaskInfo(taskInfos);
        }else if(BusinessCode.RECALL_SEND.getCode().equals(context.getCode())){
            processModel.setMessageTemplateEntity(messageTemplateEntity);
        }
    }


    private List<TaskInfo> assembleTaskInfo(SendTaskModel sendTaskModel, MessageTemplateEntity messageTemplate) {
        List<MessageParam> messageParamList = sendTaskModel.getMessageParamList();
        List<TaskInfo> taskInfoList = new ArrayList<>();

        for (MessageParam messageParam : messageParamList) {
            TaskInfo taskInfo = TaskInfo.builder()
                    .messageTemplateId(messageTemplate.getId())
                    .businessId(TaskInfoUtils.generateBusinessId(messageTemplate.getId(), messageTemplate.getTemplateType()))
                    .receiver(new HashSet<>(Arrays.asList(messageParam.getReceiver().split(String.valueOf(StrUtil.C_COMMA)))))
                    .idType(messageTemplate.getIdType())
                    .sendChannel(messageTemplate.getSendChannel())
                    .templateType(messageTemplate.getTemplateType())
                    .msgType(messageTemplate.getMsgType())
                    .sendAccount(messageTemplate.getSendAccount())
                    .contentModel(getContentModelValue(messageTemplate, messageParam)).build();

            taskInfoList.add(taskInfo);
        }
        return taskInfoList;
    }


    public static ContentModel getContentModelValue(MessageTemplateEntity messageTemplate, MessageParam messageParam) {
        Integer sendChannel = messageTemplate.getSendChannel();
        Class contentModelClass = ChannelType.getChanelModelClassByCode(sendChannel);

        Map<String, String> variables = messageParam.getVariables();
        JSONObject jsonObject = JSON.parseObject(messageTemplate.getMsgContent());

        //反射获取得到不同渠道对应的值
        Field[] fields = ReflectUtil.getFields(contentModelClass);
        ContentModel contentModel = (ContentModel) ReflectUtil.newInstance(contentModelClass);
        for (Field field : fields) {
            String originValue = jsonObject.getString(field.getName());

            if (StrUtil.isNotBlank(originValue)) {
                String resultValue = ContentHolderUtil.replaceHolder(originValue, variables);
                Object object = JSONUtil.isTypeJSONObject(resultValue)?JSONUtil.toBean(resultValue,field.getType()):resultValue;
                ReflectUtil.setFieldValue(contentModel, field, object);
            }
        }

        String url = (String) ReflectUtil.getFieldValue(contentModel, LINK_NAME);
        if (StrUtil.isNotBlank(url)) {
            String resultUrl = TaskInfoUtils.generateUrl(url, messageTemplate.getId(), messageTemplate.getTemplateType());
            ReflectUtil.setFieldValue(contentModel, LINK_NAME, resultUrl);
        }

        return contentModel;
    }

}
