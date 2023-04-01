package com.zz.messagepush.cron.service;

import com.zz.messagepush.common.domain.ResponseResult;
import com.zz.messagepush.cron.domain.dto.XxlJobInfoDTO;

/**
 * @Description 定时任务服务
 * @Author 张卫刚
 * @Date Created on 2023/3/31
 */

public interface CronTaskService {


    /**
     * 新增修改定时任务
     * 没有新增，有则修改
     *
     * @param xxlJobInfoDTO
     * @return
     */
    void saveCronTask(XxlJobInfoDTO xxlJobInfoDTO);

    /**
     * 根据定时任务id删除定时任务
     * @param taskId
     * @return
     */
    void deleteCronTask(Integer taskId);


    /**
     * 启动定时任务
     * @param taskId
     */
    void startCronTask(Integer taskId);


    /**
     * 停止定时任务
     * @param taskId
     */
    void stopCronTask(Integer taskId);
}
