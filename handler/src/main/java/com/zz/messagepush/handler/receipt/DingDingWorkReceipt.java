package com.zz.messagepush.handler.receipt;

import com.zz.messagepush.support.utils.AccountUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

/**
 * @Description 拉取钉钉工作消息回执信息
 * @Author 张卫刚
 * @Date Created on 2023/5/10
 */
@Component
public class DingDingWorkReceipt {


    private static final String URL = "https://oapi.dingtalk.com/topapi/message/corpconversation/getsendresult";

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Autowired
    private AccountUtils accountUtils;


    public void pull(){

    }
}
