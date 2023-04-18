package com.zz.messagepush.support.exception;

import com.zz.messagepush.common.enums.RespStatusEnum;
import com.zz.messagepush.support.pipeline.ProcessContext;
import com.zz.messagepush.support.pipeline.ProcessModel;

/**
 * @Description 流程处理异常
 * @Author 张卫刚
 * @Date Created on 2023/4/17
 */
public class ProcessException extends RuntimeException {


    private static final long serialVersionUID = -7560562065369525659L;
    private final ProcessContext<ProcessModel> processContext;

    public ProcessException(ProcessContext<ProcessModel> processContext) {
        super();
        this.processContext = processContext;
    }

    public ProcessException(ProcessContext<ProcessModel> processContext, Throwable cause) {
        super(cause);
        this.processContext = processContext;
    }

    @Override
    public String getMessage() {
        if (processContext != null) {
            return processContext.getResponse().getMsg();
        } else {
            return RespStatusEnum.CONTEXT_IS_NULL.getDescription();
        }
    }

    public ProcessContext<ProcessModel> getProcessContext() {
        return processContext;
    }
}
