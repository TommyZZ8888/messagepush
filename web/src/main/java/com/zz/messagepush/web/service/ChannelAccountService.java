package com.zz.messagepush.web.service;

import com.zz.messagepush.support.domain.entity.ChannelAccountEntity;

import java.util.List;

/**
 * @author DELL
 * @description 针对表【channel_account(渠道账号信息)】的数据库操作Service
 * @createDate 2023-03-08 15:17:51
 */

public interface ChannelAccountService {

    /**
     * 保存/修改渠道账户信息
     *
     * @param channelAccountEntity
     * @return
     */
    ChannelAccountEntity save(ChannelAccountEntity channelAccountEntity);

    /**
     * 根据渠道标识查询账号信息
     *
     * @param channelType
     * @return
     */
    List<ChannelAccountEntity> queryByChannelType(Integer channelType,String creator);

    /**
     * 列表信息 无条件
     * @return
     */
    List<ChannelAccountEntity> list(String creator);

    /**
     * 软删除
     * @param ids
     */
    void deleteByIds(List<Long> ids);
}
