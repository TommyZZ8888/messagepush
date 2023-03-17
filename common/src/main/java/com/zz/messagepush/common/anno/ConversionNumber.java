package com.zz.messagepush.common.anno;

import com.zz.messagepush.common.constant.CommonConstant;

import java.lang.annotation.*;

/**
 * @Description
 * @Author 张卫刚
 * @Date Created on 2023/3/16
 */
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ConversionNumber {
    //用于注解  序列化或者反序列化
    int value() default CommonConstant.REMOVE_THE_DECIMAL_POINT;
}
