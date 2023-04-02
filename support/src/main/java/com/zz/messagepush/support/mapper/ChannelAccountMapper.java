package com.zz.messagepush.support.mapper;

import com.zz.messagepush.support.domain.entity.ChannelAccountEntity;
import org.springframework.data.jpa.repository.JpaRepository;

/**
* @author DELL
* @description 针对表【channel_account(渠道账号信息)】的数据库操作Mapper
* @createDate 2023-03-08 15:17:51
* @Entity com.zz.messagepush.support.domain.entity.ChannelAccount
*/
public interface ChannelAccountMapper extends JpaRepository<ChannelAccountEntity,Long> {


}
