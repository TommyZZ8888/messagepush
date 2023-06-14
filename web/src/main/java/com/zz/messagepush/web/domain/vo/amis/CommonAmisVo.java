package com.zz.messagepush.web.domain.vo.amis;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Description amis的通用转化类
 * @Author 张卫刚
 * @Date Created on 2023/6/14
 */

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CommonAmisVo {

private String type;

private String label;

private String name;

private String value;

private Boolean required;
}
