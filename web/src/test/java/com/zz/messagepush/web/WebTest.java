package com.zz.messagepush.web;

import com.zz.messagepush.common.domain.dto.TaskInfo;
import com.zz.messagepush.handler.handler.impl.SmsHandler;
import com.zz.messagepush.support.domain.entity.SmsRecordEntity;
import com.zz.messagepush.support.mapper.SmsRecordMapper;
import com.zz.messagepush.web.messagequeue.kafaka.UserLogProducer;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.HashSet;

/**
 * @Description
 * @Author 张卫刚
 * @Date Created on 2023/3/13
 */

@SpringBootTest
public class WebTest {

    @Autowired
    private SmsRecordMapper smsRecordMapper;


    @Autowired
    private UserLogProducer productor;

    @Resource
    private SmsHandler smsHandler;


    @Test
    void context() throws Exception {
        TaskInfo build = TaskInfo.builder().receiver(new HashSet<>(Arrays.asList("15255926173")))
                .messageTemplateId(1728494L).build();
        smsHandler.doHandler(build);
//        productor.sendLog("topic");
    }

    @Test
    void test() {
        SmsRecordEntity build = SmsRecordEntity.builder().supplierId(100).msgContent("ssss")
                .messageTemplateId(154522L).supplierName("new sss").build();
        int insert = smsRecordMapper.insert(build);
        System.out.println(insert);


        //        CONSTRAINT SPRING_SESSION_ATTRIBUTES_PK PRIMARY KEY (SESSION_PRIMARY_ID, ATTRIBUTE_NAME),
//        CONSTRAINT SPRING_SESSION_ATTRIBUTES_FK FOREIGN KEY (SESSION_PRIMARY_ID) REFERENCES SPRING_SESSION(PRIMARY_ID) ON DELETE CASCADE

//        CREATE UNIQUE INDEX SPRING_SESSION_IX1 ON SPRING_SESSION (SESSION_ID);
//        CREATE INDEX SPRING_SESSION_IX2 ON SPRING_SESSION (EXPIRY_TIME);
//        CREATE INDEX SPRING_SESSION_IX3 ON SPRING_SESSION (PRINCIPAL_NAME);
    }








}
