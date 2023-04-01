package com.zz.messagepush.support.service.impl;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.yulichang.wrapper.MPJLambdaWrapper;
import com.zz.messagepush.common.constant.AustinConstant;
import com.zz.messagepush.common.domain.PageResult;
import com.zz.messagepush.common.enums.AuditStatus;
import com.zz.messagepush.common.enums.MessageStatus;
import com.zz.messagepush.common.utils.BeanUtil;
import com.zz.messagepush.common.utils.PageUtil;
import com.zz.messagepush.support.domain.dto.MessageTemplateParamDTO;
import com.zz.messagepush.support.domain.entity.MessageTemplateEntity;
import com.zz.messagepush.support.mapper.MessageTemplateMapper;
import com.zz.messagepush.support.service.MessageTemplateService;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Override
    public PageResult<MessageTemplateEntity> queryNotDeletedList(MessageTemplateParamDTO paramDTO) {
        Page<MessageTemplateEntity> page = PageUtil.convert2QueryPage(paramDTO);
        MPJLambdaWrapper<MessageTemplateEntity> wrapper = new MPJLambdaWrapper<>();
        wrapper.selectAll(MessageTemplateEntity.class).eq(MessageTemplateEntity::getIsDeleted, AustinConstant.FALSE);
        Page<MessageTemplateEntity> messageTemplateEntityPage = messageTemplateMapper.selectPage(page, wrapper);
        return PageUtil.convert2PageResult(messageTemplateEntityPage);
    }

    @Override
    public Long notDeletedCount() {
        MPJLambdaWrapper<MessageTemplateEntity> lambdaWrapper = new MPJLambdaWrapper<>();
        lambdaWrapper.select(MessageTemplateEntity::getId).eq(MessageTemplateEntity::getIsDeleted, AustinConstant.FALSE);
        return messageTemplateMapper.selectCount(lambdaWrapper);
    }

    @Override
    public void saveOrUpdate(MessageTemplateParamDTO messageTemplateParamDTO) {
        MessageTemplateEntity copy = BeanUtil.copy(messageTemplateParamDTO, MessageTemplateEntity.class);
        if (messageTemplateParamDTO.getId() == null) {
            MessageTemplateEntity messageTemplateEntity = initStatus(copy);
            messageTemplateMapper.insert(messageTemplateEntity);
            return;
        }
        messageTemplateMapper.updateById(copy);
    }

    @Override
    public void delete(List<Long> ids) {
        ids.forEach(item -> messageTemplateMapper.deleteById(item));
    }

    @Override
    public MessageTemplateEntity queryById(Long id) {
        return messageTemplateMapper.selectById(id);
    }

    @Override
    public void copy(Long id) {
        MessageTemplateEntity messageTemplateEntity = messageTemplateMapper.selectById(id);
        MessageTemplateEntity targetEntity = new MessageTemplateEntity();
        BeanUtil.copyProperties(messageTemplateEntity, targetEntity);
        targetEntity.setId(null);
        messageTemplateMapper.insert(targetEntity);
    }

    public MessageTemplateEntity initStatus(MessageTemplateEntity copy) {
        copy.setMsgStatus(MessageStatus.INIT.getCode()).setAuditStatus(AuditStatus.WAIT_AUDIT.getCode())
                .setCreator("Java3y").setUpdator("Java3y").setTeam("公众号Java3y").setAuditor("3y")
                .setDeduplicationTime(AustinConstant.FALSE).setIsNightShield(AustinConstant.FALSE)
                .setCreated(new Date()).setUpdated(new Date())
                .setIsDeleted(AustinConstant.FALSE);
        return copy;
    }

//    @Override
//    public BasicResultVO startCronTask(Long id) {
//        // 1.修改模板状态
//        MessageTemplate messageTemplate = messageTemplateDao.findById(id).get();
//
//        // 2.动态创建定时任务并启动
//        XxlJobInfo xxlJobInfo = XxlJobUtils.buildXxlJobInfo(messageTemplate);
//
//        BasicResultVO basicResultVO = cronTaskService.saveCronTask(xxlJobInfo);
//        // basicResultVO.getData()
//        //cronTaskService.startCronTask()
//
//        MessageTemplate clone = ObjectUtil.clone(messageTemplate).setMsgStatus(MessageStatus.RUN.getCode()).setUpdated(Math.toIntExact(DateUtil.currentSeconds()));
//        messageTemplateDao.save(clone);
//        return BasicResultVO.success();
//    }
//
//    @Override
//    public void stopCronTask(Long id) {
//        // 1.修改模板状态
//        MessageTemplateEntity messageTemplate = messageTemplateDao.findById(id).get();
//        MessageTemplateEntity clone = ObjectUtil.clone(messageTemplate).setMsgStatus(MessageStatus.STOP.getCode()).setUpdated(Math.toIntExact(DateUtil.currentSeconds()));
//        messageTemplateDao.save(clone);
//
//        // 2.暂停定时任务
//        return cronTaskService.stopCronTask(clone.getCronTaskId());
//    }
}
