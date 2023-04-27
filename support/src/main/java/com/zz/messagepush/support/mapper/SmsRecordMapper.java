package com.zz.messagepush.support.mapper;

import com.zz.messagepush.support.domain.entity.SmsRecordEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
* @author DELL
* @description 针对表【sms_record(短信记录信息)】的数据库操作Mapper
* @createDate 2023-03-08 15:21:22
* @Entity com.zz.messagepush.support.domain.entity.SmsRecord
*/


public interface SmsRecordMapper extends JpaRepository<SmsRecordEntity,Long> {


    /**
     * 通过日期和手机号找到发送记录
     * @param phone 手机号
     * @param sendDate 发送日期
     * @return list
     */
   List<SmsRecordEntity> findByPhoneAndSendDate(Long phone,Integer sendDate);

}
