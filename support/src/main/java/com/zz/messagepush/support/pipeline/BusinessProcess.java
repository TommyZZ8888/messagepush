package com.zz.messagepush.support.pipeline;

/**
 * @Description
 * @Author 张卫刚
 * @Date Created on 2023/3/14
 */
public interface BusinessProcess<T extends ProcessModel> {

    /**
     * 业务执行接口 真正处理逻辑
     *
     * @param context
     */
    void process(ProcessContext<T> context);
}
