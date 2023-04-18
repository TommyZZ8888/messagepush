package com.zz.messagepush.handler.enums;

/**
 * @Description 限流枚举
 * @Author 张卫刚
 * @Date Created on 2023/4/18
 */
public enum RateLimitStrategy {

    REQUEST_RATE_LIMIT(10, "根据真实请求数限流"),

    SEND_USER_NUM_RATE_LIMIT(20,"根据发送用户数请求数限流"),
    ;

    private Integer code;

    private String description;

    RateLimitStrategy (Integer code, String description) {
        this.description = description;
        this.code = code;
    }

    public Integer getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }

    public void setCode(Integer code){
        this.code = code;
    }

    public void setDescription(String description){
        this.description = description;
    }
 }