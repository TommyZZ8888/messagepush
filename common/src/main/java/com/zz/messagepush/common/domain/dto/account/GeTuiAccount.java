package com.zz.messagepush.common.domain.dto.account;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Description 创建个腿账号时的元信息
 * @Author 张卫刚
 * @Date Created on 2023/4/23
 */

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GeTuiAccount {

    private String appId;

    private String appKey;

    private String masterSecret;
}
