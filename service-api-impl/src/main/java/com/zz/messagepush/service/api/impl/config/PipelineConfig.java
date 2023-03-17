package com.zz.messagepush.service.api.impl.config;

import com.zz.messagepush.service.api.enums.BusinessCode;
import com.zz.messagepush.service.api.impl.action.AssembleAction;
import com.zz.messagepush.service.api.impl.action.ParamAction;
import com.zz.messagepush.service.api.impl.action.SendMqAction;
import com.zz.messagepush.support.pipeline.BusinessProcess;
import com.zz.messagepush.support.pipeline.ProcessHandler;
import com.zz.messagepush.support.pipeline.ProcessTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Description
 * @Author 张卫刚
 * @Date Created on 2023/3/17
 */

@Configuration
public class PipelineConfig {


    /**
     * 普通发送执行流程
     * 1.参数校验
     * 2.组装参数
     * 3.发送消息至MQ
     */
    @Bean("commonSendTemplate")
    public ProcessTemplate commonSendTemplate() {
        ProcessTemplate processTemplate = new ProcessTemplate();
        List<BusinessProcess> processList = new ArrayList<>();

        processList.add(paramAction());
        processList.add(assembleAction());
        processList.add(sendMqAction());

        processTemplate.setProcessList(processList);
        return processTemplate;
    }


    /**
     * pipeline流程控制器
     * 目前暂定只有普通发送的流程
     * 后续扩展则增加BusinessCode和ProcessTemplate
     */
    @Bean
    public ProcessHandler processHandle() {
        ProcessHandler processHandler = new ProcessHandler();
        Map<String, ProcessTemplate> map = new HashMap<>();
        map.put(BusinessCode.COMMON_SEND.getCode(), commonSendTemplate());
        processHandler.setTemplateConfig(map);
        return processHandler;
    }


    /**
     * 组装参数的action
     */
    @Bean
    public AssembleAction assembleAction() {
        return new AssembleAction();
    }

    /**
     * 参数校验 action
     */
    @Bean
    public ParamAction paramAction() {
        return new ParamAction();
    }

    /**
     * 发送消息至MQ的action
     */
    @Bean
    public SendMqAction sendMqAction() {
        return new SendMqAction();
    }

}
