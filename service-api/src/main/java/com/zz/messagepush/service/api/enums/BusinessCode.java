package com.zz.messagepush.service.api.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

/**
 * @Description
 * @Author 张卫刚
 * @Date Created on 2023/3/16
 */


@Getter
@ToString
@AllArgsConstructor
public enum BusinessCode {


    COMMON_SEND("send", "普通发送"),

    RECALL_SEND("recall", "撤回消息"),
    ;

    private final String code;

    private final String description;

}
