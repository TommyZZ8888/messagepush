package com.zz.messagepush.common.enums;

/**
 * @Description
 * @Author 张卫刚
 * @Date Created on 2023/3/30
 */
public enum MessageStatus {

    /**
     * 10.新建 20.停用 30.启用 40.等待发送 50.发送中 60.发送成功 70.发送失败
     */

    INIT(10, "新建"),

    STOP(20, "停用"),

    RUN(30, "启用"),

    PENDING(40, "等待发送"),

    SENDING(50, "发送中"),

    SEND_SUCCESS(60, "发送成功"),

    SEND_FAIL(70, "发送失败"),
    ;

    private Integer code;
    private String description;

    MessageStatus(Integer code, String description) {
        this.code = code;
        this.description = description;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public Integer getCode() {
        return code;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
