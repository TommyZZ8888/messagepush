package com.zz.messagepush.common.enums;

import com.zz.messagepush.common.domain.dto.*;

/**
 * 发送渠道类型枚举
 *
 * @Author 张卫刚
 * @Date Created on 2023/3/13
 */
public enum ChannelType {
    IM(10, "IM(站内信)", ImContentModel.class, "im"),

    PUSH(20, "push(通知栏)", PushContentModel.class, "push"),

    SMS(30, "sms(短信)", SmsContentModel.class, "sms"),

    EMAIL(40, "email(邮件)", EmailContentModel.class, "email"),

    OFFICIAL_ACCOUNT(50, "OfficialAccount(服务号)", OfficialAccountContentModel.class, "official_accounts"),

    MINI_PROGRAM(60, "miniProgram(小程序)", MiniProgramContentModel.class, "mini_program"),

    ENTERPRISE_WE_CHAT(70, "enterpriseWeChat(企业微信)", EnterpriseWeChatContentModel.class, "enterprise_we_chat"),
    ;

    private Integer code;

    private String description;

    /**
     * 内容模型class
     */
    private Class contentModelClass;

    /**
     * 英文标识
     */
    private String codeEn;

    ChannelType(Integer code, String description, Class contentModelClass, String codeEn) {
        this.code = code;
        this.description = description;
        this.contentModelClass = contentModelClass;
        this.codeEn = codeEn;
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

    public String getCodeEn() {
        return codeEn;
    }

    public void setCodeEn(String codeEn) {
        this.codeEn = codeEn;
    }


    /**
     * 通过code获取class
     *
     * @param code
     * @return
     */
    public static Class getChanelModelClassByCode(Integer code) {
        ChannelType[] values = values();
        for (ChannelType value : values) {
            if (value.getCode().equals(code)) {
                return value.getContentModelClass();
            }
        }
        return null;
    }


    /**
     * 通过code获取enum
     *
     * @param code
     * @return
     */
    public static ChannelType getEnumByCode(Integer code) {
        ChannelType[] values = values();
        for (ChannelType value : values) {
            if (value.getCode().equals(code)) {
                return value;
            }
        }
        return null;
    }

}
