package com.zz.messagepush.common.enums;

/**
 * @Description 发送的消息类型
 * @Author 张卫刚
 * @Date Created on 2023/3/13
 */
public enum MessageType {

    NOTICE(10,"通知类信息"),

    MARKETING(20,"营销类信息"),

    AUTH_CODE(30,"验证码信息"),
    ;

    private Integer code;

    private String description;

    MessageType(Integer code, String description) {
        this.code = code;
        this.description = description;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
