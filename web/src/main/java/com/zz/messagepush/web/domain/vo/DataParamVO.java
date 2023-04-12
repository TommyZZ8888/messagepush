package com.zz.messagepush.web.domain.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
/**
 * @Description
 * @Author 张卫刚
 * @Date Created on 2023/4/10
 */


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DataParamVO {

    /**
     * 传入用户id查询用户链路数据
     */
    private String receiver;

    /**
     * 业务id
     * 如果传入的是模板id，则生成当天的业务id
     */
    private String businessId;
}
