package com.zz.messagepush.service.api.impl.config;

import com.zz.messagepush.service.api.enums.BusinessCode;
import com.zz.messagepush.service.api.impl.action.AfterParamCheckAction;
import com.zz.messagepush.service.api.impl.action.AssembleAction;
import com.zz.messagepush.service.api.impl.action.PreParamCheckAction;
import com.zz.messagepush.service.api.impl.action.SendMqAction;
import com.zz.messagepush.support.pipeline.BusinessProcess;
import com.zz.messagepush.support.pipeline.ProcessHandler;
import com.zz.messagepush.support.pipeline.ProcessTemplate;
import org.apache.kafka.common.network.Send;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.*;

/**
 * @Description
 * @Author 张卫刚
 * @Date Created on 2023/3/17
 */

@Configuration
public class PipelineConfig {


    @Autowired
    private PreParamCheckAction preParamCheckAction;

    @Autowired
    private AssembleAction assembleAction;

    @Autowired
    private SendMqAction sendMqAction;

    @Autowired
    private AfterParamCheckAction afterParamCheckAction;

    /**
     * 普通发送执行流程
     * 1.参数校验
     * 2.组装参数
     * 3.发送消息至MQ
     */
    @Bean("commonSendTemplate")
    public ProcessTemplate commonSendTemplate() {
        ProcessTemplate processTemplate = new ProcessTemplate();

        processTemplate.setProcessList(Arrays.asList(preParamCheckAction, assembleAction, sendMqAction, afterParamCheckAction));
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
}
