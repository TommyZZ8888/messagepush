package com.zz.messagepush.cron.domain.vo;

/**
 * @Description 每一行csv的记录
 * @Author 张卫刚
 * @Date Created on 2023/4/4
 */

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CrowdInfoVO {

    /**
     * 接收者
     */
    private String id;

    /**
     * 参数
     */
    private Map<String, String> params;


}
