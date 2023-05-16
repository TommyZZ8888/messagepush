package com.zz.messagepush.common.domain.dto.account;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Description 飞书机器人账号信息
 * @Author 张卫刚
 * @Date Created on 2023/5/16
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FeiShuRobotAccount {

private String webhook;
}
