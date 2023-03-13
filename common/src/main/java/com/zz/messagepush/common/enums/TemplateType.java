package com.zz.messagepush.common.enums;

/**
 * @Description 消息模板类型
 * @Author 张卫刚
 * @Date Created on 2023/3/13
 */
public enum TemplateType {

    OPERATION(10,"运营类的模板"),

    TECHNOLOGY(20,"技术类的模板"),

    ;

    private Integer code;

    private String description;

    TemplateType(Integer code, String description) {
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
