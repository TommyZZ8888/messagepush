package com.zz.messagepush.common.domain;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
public class PageResult<T> {

    @ApiModelProperty("当前页")
    private Long page;

    @ApiModelProperty("每页数量")
    private Long pageSize;

    @ApiModelProperty("总记录数")
    private Long total;

    @ApiModelProperty("总页数")
    private Long pages;

    @ApiModelProperty("结果集")
    private List<T> list;

}
