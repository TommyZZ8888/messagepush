package com.zz.messagepush.common.handler;


import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * mybaits-plus自动填充
 */
@Component
@Slf4j
public class MyMetaObjectHandler {
//public class MyMetaObjectHandler implements MetaObjectHandler {

//    @Override
//    public void insertFill(MetaObject metaObject) {
//        this.setFieldValByName("createTime", new Date(), metaObject)
//                .setFieldValByName("updateTime", new Date(), metaObject)
//                .setFieldValByName("isDeleted", false, metaObject);
//    }
//
//    @Override
//    public void updateFill(MetaObject metaObject) {
//        this.setFieldValByName("updateTime", new Date(), metaObject);
//    }
}
