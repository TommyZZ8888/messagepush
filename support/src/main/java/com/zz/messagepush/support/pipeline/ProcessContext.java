package com.zz.messagepush.support.pipeline;

import com.zz.messagepush.common.domain.ResponseResult;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Description 责任链上下文
 * @Author 张卫刚
 * @Date Created on 2023/3/14
 */


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProcessContext {

    /**
     * 标识责任链的code
     */
    private String code;

    /**
     * 存储责任链上下文数据的模型
     */
    private ProcessModel processModel;

    /**
     * 责任链中断的标识
     */
    private Boolean needBreak;


    ResponseResult<Object> response = ResponseResult.success();

}
