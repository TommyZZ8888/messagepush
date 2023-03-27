package com.zz.messagepush.support.utils;

import cn.monitor4all.logRecord.bean.LogDTO;
import cn.monitor4all.logRecord.service.CustomLogListener;
import com.alibaba.fastjson.JSON;
import com.zz.messagepush.common.domain.AnchorInfo;
import com.zz.messagepush.common.domain.LogParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @Description 所有的日志都存在
 * @Author 张卫刚
 * @Date Created on 2023/3/24
 */
@Slf4j
@Component
public class LogUtils extends CustomLogListener {


    /**
     * 方法的切面 @OperationLog产生
     *
     * @param logDTO
     * @throws Exception
     */
    @Override
    public void createLog(LogDTO logDTO) {
        log.info(JSON.toJSONString(logDTO));
    }

    /**
     * 记录当前对象信息
     *
     * @param logParam
     */
    public static void print(LogParam logParam) {
        logParam.setTimestamp(System.currentTimeMillis());
        log.info(JSON.toJSONString(logParam));
    }

    /**
     * 记录打点信息
     *
     * @param anchorInfo
     */
    public static void print(AnchorInfo anchorInfo) {
        anchorInfo.setTimeStamp(System.currentTimeMillis());
        log.info(JSON.toJSONString(anchorInfo));
    }

    /**
     * 记录打点信息和对象信息
     *
     * @param logParam
     * @param anchorInfo
     */
    public static void print(LogParam logParam, AnchorInfo anchorInfo) {
        print(logParam);
        print(anchorInfo);
    }
}
