package com.zz.messagepush.web.messagequeue.kafaka;

import com.alibaba.fastjson.JSON;
import com.zz.messagepush.web.messagequeue.UserLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

/**
 * @Description
 * @Author 张卫刚
 * @Date Created on 2023/3/14
 */
@Component
public class UserLogProducer {

    @Autowired
    private KafkaTemplate kafkaTemplate;

    public void sendLog(String userId) {
        UserLog userLog = new UserLog();
        userLog.setUserName("jhp").setUserId(userId).setState("0");
        System.err.println("发送用户日志数据:" + userLog);
        kafkaTemplate.send("austin", JSON.toJSONString(userLog));
    }


}
