package com.zz.messagepush.support.service.impl;

import cn.hutool.core.date.DateUtil;
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
}
