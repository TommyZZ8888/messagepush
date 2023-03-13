package com.zz.messagepush.support.mapper;

import com.github.yulichang.base.MPJBaseMapper;
import com.zz.messagepush.support.domain.entity.SmsRecordEntity;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
* @author DELL
* @description 针对表【sms_record(短信记录信息)】的数据库操作Mapper
* @createDate 2023-03-08 15:21:22
* @Entity com.zz.messagepush.support.domain.entity.SmsRecord
*/

@Mapper
public interface SmsRecordMapper extends MPJBaseMapper<SmsRecordEntity> {

    Integer insertBatchSomeColumn(List<SmsRecordEntity> entities);
}
