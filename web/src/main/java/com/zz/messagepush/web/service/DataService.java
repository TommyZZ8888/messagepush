package com.zz.messagepush.web.service;

import com.zz.messagepush.web.domain.vo.amis.EchartsVO;
import com.zz.messagepush.web.domain.vo.amis.UserTimeLineVO;

/**
 * @Description
 * @Author 张卫刚
 * @Date Created on 2023/4/10
 */
public interface DataService {

    /**
     * 获取全链路追踪 用户维度信息
     *
     * @param receiver
     * @return
     */
    UserTimeLineVO getTraceUserInfo(String receiver);


    /**
     * 获取全链路追踪 消息模板维度信息
     *
     * @param businessId
     * @return
     */
    EchartsVO getTraceMessageTemplateInfo(String businessId);

}
