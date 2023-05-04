package com.zz.messagepush.common.domain.dto.account;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Description YunPianSmsAccount
 * @Author 张卫刚
 * @Date Created on 2023/5/4
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class YunPianSmsAccount {

    /**
     * [{"sms_20":{"url":"https://sms.yunpian.com/v2/sms/tpl_batch_send.json",
     * "apikey":"ca55d4c8544444444444622221b5cd7",
     * "tpl_id":"533332222282",
     * "supplierId":20,"supplierName":"云片"}}]
     */

    private String url;

    private String apikey;

    private String tplId;

    /**
     * 标识渠道商id
     */
    private String supplierId;

    private String supplierName;
}
