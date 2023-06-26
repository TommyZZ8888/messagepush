package com.zz.messagepush.common.anno;

import java.lang.annotation.*;

/**
 * @Description 统一返回注解
 * @Author 张卫刚
 * @Date Created on 2023/6/26
 */
@Target({ElementType.ANNOTATION_TYPE,ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface AustinResult {

}
