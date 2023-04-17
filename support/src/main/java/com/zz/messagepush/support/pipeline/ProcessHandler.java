package com.zz.messagepush.support.pipeline;

import cn.hutool.core.collection.CollUtil;
import com.alibaba.fastjson.JSONObject;
import com.zz.messagepush.common.domain.ResponseResult;
import com.zz.messagepush.common.enums.RespStatusEnum;
import com.zz.messagepush.common.exception.ErrorException;
import com.zz.messagepush.support.exception.ProcessException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.util.List;
import java.util.Map;

/**
 * @Description
 * @Author 张卫刚
 * @Date Created on 2023/3/16
 */

@Service
@Slf4j
public class ProcessHandler {

    /**
     * 模板映射
     */
    private Map<String, ProcessTemplate> templateConfig = null;

    /**
     * 执行责任链
     *
     * @param context
     * @return
     */
    public ProcessContext process(ProcessContext context) {
        try {
            preCheck(context);
        } catch (ProcessException e) {
            context = e.getProcessContext();
            return context;
        }
        /**
         * 遍历流程节点
         */
        List<BusinessProcess> processList = templateConfig.get(context.getCode()).getProcessList();
        for (BusinessProcess businessProcess : processList) {
            businessProcess.process(context);
            if (context.getNeedBreak()) {
                break;
            }
        }
        return context;
    }


    private Boolean preCheck(ProcessContext context) {
        if (context == null) {
            context = new ProcessContext();
            context.setResponse(ResponseResult.fail(RespStatusEnum.CONTEXT_IS_NULL.getDescription()));
            throw new ProcessException(context);
        }

        //业务代码
        String businessCode = context.getCode();
        if (StringUtils.isBlank(businessCode)) {
            context.setResponse(ResponseResult.fail(RespStatusEnum.BUSINESS_CODE_IS_NULL.getDescription()));
            throw new ErrorException(context.getResponse().getMsg());
        }

        //执行模板
        ProcessTemplate processTemplate = templateConfig.get(businessCode);
        if (processTemplate == null) {
            context.setResponse(ResponseResult.fail(RespStatusEnum.PROCESS_TEMPLATE_IS_NULL.getDescription()));
            throw new ProcessException(context);
        }

        //执行模板列表
        List<BusinessProcess> processList = processTemplate.getProcessList();
        if (CollUtil.isEmpty(processList)) {
            context.setResponse(ResponseResult.fail(RespStatusEnum.PROCESS_LIST_IS_NULL.getDescription()));
            throw new ProcessException(context);
        }
        return true;
    }


    public Map<String, ProcessTemplate> getTemplateConfig() {
        return templateConfig;
    }

    public void setTemplateConfig(Map<String, ProcessTemplate> templateConfig) {
        this.templateConfig = templateConfig;
    }

}
