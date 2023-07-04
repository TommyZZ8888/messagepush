package com.zz.messagepush.common.domain.dto.account.sms;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Description LinTongSmsAccount
 * @Author 张卫刚
 * @Date Created on 2023/7/4
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LinTongSmsAccount extends SmsAccount{

    private String url;

    private String userName;

    private String password;

    private Integer supplierId;

    private String supplierName;
}
