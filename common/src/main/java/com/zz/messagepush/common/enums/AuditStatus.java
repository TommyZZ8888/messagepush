package com.zz.messagepush.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

/**
 * @Description
 * @Author 张卫刚
 * @Date Created on 2023/3/30
 */

public enum AuditStatus {

    /**
     * 10.待审核 20.审核成功 30.被拒绝
     */

    WAIT_AUDIT(10, "待审核"),
    AUDIT_SUCCESS(20, "审核成功"),
    AUDIT_REJECT(30, "被拒绝"),
    ;

    private Integer code;
    private String description;

    AuditStatus(Integer code, String description) {
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
