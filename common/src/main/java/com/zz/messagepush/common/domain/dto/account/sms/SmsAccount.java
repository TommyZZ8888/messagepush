package com.zz.messagepush.common.domain.dto.account.sms;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Description SmsAccount
 * @Author 张卫刚
 * @Date Created on 2023/6/13
 */

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SmsAccount {

    /**
     * 标识渠道商id
     */
    protected Integer supplierId;

    /**
     * 标识渠道商名字
     */
    protected String supplierName;

    /**
     * （类名）定位到具体的处理下发/回执逻辑
     * 依据scriptName对应具体的某一个短信账号
     */
    protected String scriptName;

}
