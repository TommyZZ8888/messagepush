package com.zz.messagepush.handler.script;

import com.zz.messagepush.handler.domain.sms.SmsParam;
import com.zz.messagepush.support.domain.entity.SmsRecordEntity;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Description
 * @Author 张卫刚
 * @Date Created on 2023/3/13
 */
@Service
public interface SmsService {

    /**
     * 发送短信
     * @param smsParamDTO 发送短信参数
     * @return 渠道商发送接口返回值
     */
    List<SmsRecordEntity> send(SmsParam smsParamDTO);


    /**
     * 拉取回执
     * @param scriptName 标识账号的脚本名
     * @return 渠道商拉取回执接口返回值
     */
    List<SmsRecordEntity> pull(String scriptName);

}
