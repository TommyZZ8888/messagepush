package com.zz.messagepush.support.domain.entity;

import java.io.Serializable;
import lombok.Data;

/**
 * 短信记录信息
 * @TableName sms_record
 */
@Data
public class SmsRecordEntity implements Serializable {
    /**
     * 
     */
    private Long id;

    /**
     * 消息模板ID
     */
    private Long messageTemplateId;

    /**
     * 手机号
     */
    private Long phone;

    /**
     * 发送短信渠道商的ID
     */
    private Integer supplierId;

    /**
     * 发送短信渠道商的名称
     */
    private String supplierName;

    /**
     * 短信发送的内容
     */
    private String msgContent;

    /**
     * 下发批次的ID
     */
    private String seriesId;

    /**
     * 计费条数
     */
    private Integer chargingNum;

    /**
     * 回执内容
     */
    private String reportContent;

    /**
     * 短信状态： 10.发送 20.成功 30.失败
     */
    private Integer status;

    /**
     * 发送日期：20211112
     */
    private Integer sendDate;

    /**
     * 创建时间
     */
    private Integer created;

    /**
     * 更新时间
     */
    private Integer updated;

    private static final long serialVersionUID = 1L;
}