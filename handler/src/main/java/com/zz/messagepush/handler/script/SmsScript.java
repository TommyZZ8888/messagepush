package com.zz.messagepush.handler.script;

import com.tencentcloudapi.common.exception.TencentCloudSDKException;
import com.zz.messagepush.common.domain.dto.SmsParam;
import com.zz.messagepush.support.domain.entity.SmsRecordEntity;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Description
 * @Author 张卫刚
 * @Date Created on 2023/3/13
 */
@Service
public interface SmsScript {

    /**
     * 发送短信
     * @param smsParamDTO 发送短信参数
     * @return 渠道商接口返回值
     */
    List<SmsRecordEntity> send(SmsParam smsParamDTO) throws Exception;

}
