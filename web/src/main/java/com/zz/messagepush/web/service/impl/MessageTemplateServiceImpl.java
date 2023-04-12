package com.zz.messagepush.web.service.impl;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.zz.messagepush.common.constant.AustinConstant;
import com.zz.messagepush.common.domain.ResponseResult;
import com.zz.messagepush.common.enums.AuditStatus;
import com.zz.messagepush.common.enums.MessageStatus;
import com.zz.messagepush.common.enums.RespStatusEnum;
import com.zz.messagepush.common.utils.BeanUtil;
import com.zz.messagepush.cron.domain.dto.XxlJobInfoDTO;
import com.zz.messagepush.cron.domain.entity.XxlJobInfo;
import com.zz.messagepush.cron.service.CronTaskService;
import com.zz.messagepush.cron.utils.XxlJobUtils;
import com.zz.messagepush.support.domain.dto.MessageTemplateParamDTO;
import com.zz.messagepush.support.domain.entity.MessageTemplateEntity;
import com.zz.messagepush.support.mapper.MessageTemplateMapper;
import com.zz.messagepush.web.service.MessageTemplateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * @author DELL
 * @description 针对表【message_template(消息模板信息)】的数据库操作Service实现
 * @createDate 2023-03-08 15:21:05
 */
@Service
public class MessageTemplateServiceImpl implements MessageTemplateService {


    @Autowired
    private MessageTemplateMapper messageTemplateMapper;

    @Autowired
    private CronTaskService cronTaskService;

    @Autowired
    private XxlJobUtils xxlJobUtils;

    @Override
    public List<MessageTemplateEntity> queryNotDeletedList(MessageTemplateParamDTO paramDTO) {
        PageRequest pageRequest = PageRequest.of(paramDTO.getPageIndex() - 1, paramDTO.getPageSize());
        return messageTemplateMapper.findAllByIsDeletedEquals(AustinConstant.FALSE, pageRequest);
    }

    @Override
    public Long notDeletedCount() {
        return messageTemplateMapper.countByIsDeletedEquals(AustinConstant.FALSE);
    }

    @Override
    public void saveOrUpdate(MessageTemplateParamDTO messageTemplateParamDTO) {
        MessageTemplateEntity copy = BeanUtil.copy(messageTemplateParamDTO, MessageTemplateEntity.class);
        if (messageTemplateParamDTO.getId() == null) {
            initStatus(copy);
            messageTemplateMapper.save(copy);
            return;
        }
        messageTemplateMapper.save(copy);
    }

    @Override
    public void deleteByIds(List<Long> ids) {
        Iterable<MessageTemplateEntity> entities = messageTemplateMapper.findAllById(ids);
        entities.forEach(messageTemplate -> messageTemplate.setIsDeleted(AustinConstant.TRUE));
        for (MessageTemplateEntity entity : entities) {
            if (entity.getCronTaskId() != null && entity.getCronTaskId() > 0) {
                cronTaskService.deleteCronTask(entity.getCronTaskId());
            }
        }
        messageTemplateMapper.saveAll(entities);
    }

    @Override
    public MessageTemplateEntity queryById(Long id) {
        return messageTemplateMapper.findById(id).orElse(null);
    }

    @Override
    public void copy(Long id) {
        MessageTemplateEntity messageTemplateEntity = messageTemplateMapper.findById(id).orElse(new MessageTemplateEntity());
        MessageTemplateEntity entity = BeanUtil.copy(messageTemplateEntity, MessageTemplateEntity.class).setId(null).setCronTaskId(null);
        entity.setId(null);
        messageTemplateMapper.save(entity);
    }


    /**
     * 初始化状态信息
     * TODO 创建者 修改者 团队
     *
     * @param messageTemplate
     */
    private void initStatus(MessageTemplateEntity messageTemplate) {
        messageTemplate.setFlowId(StrUtil.EMPTY)
                .setMsgStatus(MessageStatus.INIT.getCode()).setAuditStatus(AuditStatus.WAIT_AUDIT.getCode())
                .setCreator("Java3y").setUpdator("Java3y").setTeam("公众号Java3y").setAuditor("3y")
                .setDeduplicationTime(AustinConstant.FALSE).setIsNightShield(AustinConstant.FALSE)
                .setCreated(new Date()).setUpdated(new Date())
                .setIsDeleted(AustinConstant.FALSE);
    }

    @Override
    public ResponseResult startCronTask(Long id) {
        //1.获取消息模板的信息
        MessageTemplateEntity messageTemplateEntity = messageTemplateMapper.findById(id).orElse(new MessageTemplateEntity());

        // 2.动态创建定时任务并启动
        XxlJobInfo xxlJobInfo = xxlJobUtils.buildXxlJobInfo(messageTemplateEntity);
        // 3.获取taskId（如果本身存在则复用原有id，不存在则得到新建后任务id）
        Integer cronTaskId = messageTemplateEntity.getCronTaskId();
        ResponseResult responseResult = cronTaskService.saveCronTask(BeanUtil.copy(xxlJobInfo, XxlJobInfoDTO.class));

        if (cronTaskId == null && RespStatusEnum.SUCCESS.getCode().equals(responseResult.getCode()) && responseResult.getData() != null) {
            cronTaskId = Integer.valueOf(String.valueOf(responseResult.getData()));
        }
        if (cronTaskId != null) {
            cronTaskService.startCronTask(cronTaskId);
            MessageTemplateEntity clone = BeanUtil.copy(messageTemplateEntity, MessageTemplateEntity.class).setMsgStatus(MessageStatus.RUN.getCode()).setUpdated(new Date());
            messageTemplateMapper.save(clone);
            return ResponseResult.success();
        }
        return ResponseResult.fail("fail");
    }

    //
    @Override
    public void stopCronTask(Long id) {
        // 1.修改模板状态
        MessageTemplateEntity messageTemplate = messageTemplateMapper.findById(id).orElse(new MessageTemplateEntity());
        MessageTemplateEntity clone = ObjectUtil.clone(messageTemplate).setMsgStatus(MessageStatus.STOP.getCode()).setUpdated(new Date());
        messageTemplateMapper.save(clone);

        // 2.暂停定时任务
        cronTaskService.stopCronTask(clone.getCronTaskId());
    }
}
