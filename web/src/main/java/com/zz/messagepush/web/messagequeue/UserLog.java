package com.zz.messagepush.web.messagequeue;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @Description
 * @Author 张卫刚
 * @Date Created on 2023/3/14
 */
@Data
@Accessors(chain = true)
public class UserLog {

    private String userName;

    private String userId;

    private String state;
}
