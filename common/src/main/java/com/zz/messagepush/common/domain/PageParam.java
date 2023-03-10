package com.zz.messagepush.common.domain;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;


@Data
public class PageParam {


    protected Integer pageIndex = 1;

    protected Integer pageSize = 20;

    @ApiModelProperty("是否查询总条数")
    protected Boolean searchCount;

    protected List<OrderItem> orderItemList;
}
