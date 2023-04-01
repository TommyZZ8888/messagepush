package com.zz.messagepush.cron.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

/**
 * @Description xxl-job的参数
 * @Author 张卫刚
 * @Date Created on 2023/3/31
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TaskParam {

    /**
     * 模板id
     */
    private Long messageTemplateId;

    /**
     * 执行的cron表达式
     */
    private String cron;

    /**
     * 创建者
     */
    private String creator;

    /**
     * 扩展参数
     */
    private Map<String,Object> extra;
}
