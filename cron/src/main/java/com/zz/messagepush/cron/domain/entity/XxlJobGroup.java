package com.zz.messagepush.cron.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * @Description
 * @Author 张卫刚
 * @Date Created on 2023/4/3
 */

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class XxlJobGroup {

    private Integer id;

    private String appName;

    private String title;

    /**
     * 执行器注册类型 多地址逗号分隔（0自动注册 1手动注册）
     */
    private Integer addressType;

    /**
     * 执行器地址列表（手动注册）
     */
    private String addressList;

    private Date updateTime;

    /**
     * 执行器地址列表（系统注册）
     */
    private List<String> registryList;


    public List<String> getRegistry() {
        if (addressList != null && addressList.trim().length() > 0) {
            registryList = Arrays.asList(addressList.split(","));
        }
        return registryList;
    }

}
