package com.zz.messagepush.support.mapper;

import com.zz.messagepush.support.domain.entity.ChannelAccountEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * @author DELL
 * @description 针对表【channel_account(渠道账号信息)】的数据库操作Mapper
 * @createDate 2023-03-08 15:17:51
 * @Entity com.zz.messagepush.support.domain.entity.ChannelAccount
 */
public interface ChannelAccountMapper extends JpaRepository<ChannelAccountEntity, Long> {

    /**
     * 查询 分页
     *
     * @param deleted
     * @param channelType
     * @return
     */
    List<ChannelAccountEntity> findAllByIsDeletedEqualsAndSendChannelEquals(Integer deleted, Integer channelType);

    /**
     * 统计未删除的条数
     *
     * @param deleted
     * @return
     */
    Long countByIsDeletedEquals(Integer deleted);

    /**
     * 查询 列表
     *
     * @param deleted     0：未删除 1：删除
     * @param channelType 渠道值
     * @param creator     创建者
     * @return
     */
    List<ChannelAccountEntity> findAllByIsDeletedEqualsAndCreatorEqualsAndSendChannelEquals(Integer deleted, String creator, Integer channelType);


    /**
     * 根据创建者检索相关的记录
     *
     * @param creator
     * @return
     */
    List<ChannelAccountEntity> findAllByCreatorEquals(String creator);
}
