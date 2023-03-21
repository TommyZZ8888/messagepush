package com.zz.messagepush.common.enums;

import com.zz.messagepush.common.domain.dto.*;

/**
 * 发送渠道类型枚举
 *
 * @Author 张卫刚
 * @Date Created on 2023/3/13
 */
public enum ChannelType {
    IM(10, "IM(站内信)", ImContentModel.class),

    PUSH(20, "push(通知栏)", PushContentModel.class),

    SMS(30, "sms(短信)", SmsContentModel.class),

    EMAIL(40, "email(邮件)", EmailContentModel.class),

    OFFICIAL_ACCOUNT(50, "OfficialAccount(服务号)", OfficialAccountContentModel.class),

    MINI_PROGRAM(60, "miniProgram(小程序)",MiniProgramContentModel.class),
    ;

    private Integer code;

    private String description;

    /**
     * 内容模型class
     */
    private Class contentModelClass;

    ChannelType(Integer code, String description, Class contentModelClass) {
        this.code = code;
        this.description = description;
        this.contentModelClass = contentModelClass;
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

    public Class getContentModelClass() {
        return contentModelClass;
    }

    public void setContentModelClass(Class contentModelClass) {
        this.contentModelClass = contentModelClass;
    }

    public static Class getChanelModelClassByCode(Integer code) {
        ChannelType[] values = values();
        for (ChannelType value : values) {
            if (value.getCode().equals(code)) {
                return value.getContentModelClass();
            }
        }
        return null;
    }

}
