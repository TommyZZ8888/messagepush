package com.zz.messagepush.common.enums;

/**
 * @Description 消息频闭类型
 * @Author 张卫刚
 * @Date Created on 2023/4/14
 */
public enum ShieldType {

    NIGHT_SHIELD(10, "夜间屏蔽"),

    NIGHT_NO_SHIELD(20,"夜间不屏蔽"),

    NIGHT_SHIELD_BUT_NEXT_DAY_SEND(30,"夜间屏蔽（第二天早上九点发送）")
    ;

    private Integer code;

    private String description;

    ShieldType (Integer code, String description) {
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
