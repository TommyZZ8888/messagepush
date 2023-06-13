package com.zz.messagepush.web.service.impl;

import com.zz.messagepush.common.constant.AustinConstant;
import com.zz.messagepush.common.constant.CommonConstant;
import com.zz.messagepush.support.domain.entity.ChannelAccountEntity;
import com.zz.messagepush.support.mapper.ChannelAccountMapper;
import com.zz.messagepush.web.service.ChannelAccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * @author DELL
 * @description 针对表【channel_account(渠道账号信息)】的数据库操作Service实现
 * @createDate 2023-03-08 15:17:51
 */
@Service
public class ChannelAccountServiceImpl implements ChannelAccountService {

    @Autowired
    private ChannelAccountMapper channelAccountMapper;


    @Override
    public ChannelAccountEntity save(ChannelAccountEntity channelAccountEntity) {
        if (channelAccountEntity.getId() == null) {
            channelAccountEntity.setCreated(new Date());
            channelAccountEntity.setIsDeleted(CommonConstant.FALSE);
        }
        channelAccountEntity.setUpdated(new Date());
        return channelAccountMapper.save(channelAccountEntity);
    }

    @Override
    public List<ChannelAccountEntity> queryByChannelType(Integer channelType) {
        return channelAccountMapper.findAllByIsDeletedEqualsAndSendChannelEquals(CommonConstant.FALSE, channelType);
    }

    @Override
    public List<ChannelAccountEntity> list() {
        return channelAccountMapper.findAll();
    }

    @Override
    public void deleteByIds(List<Long> ids) {
        channelAccountMapper.deleteAllById(ids);
    }
}
