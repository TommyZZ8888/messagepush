package com.zz.messagepush.common.domain;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Data
public class OrderItem {


    private String column;

    private boolean asc = true;
}
