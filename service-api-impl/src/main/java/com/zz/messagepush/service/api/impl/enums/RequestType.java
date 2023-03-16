package com.zz.messagepush.service.api.impl.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

/**
 * @Description 请求类型
 * @Author 张卫刚
 * @Date Created on 2023/3/16
 */

@Getter
@ToString
@AllArgsConstructor
public enum RequestType {

    SINGLE(10, "请求接口为 single类型"),

    BATCH(20, "请求接口为batch类型"),
    ;

    private Integer code;

    private String description;


}
