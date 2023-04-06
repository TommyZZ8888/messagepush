package com.zz.messagepush.cron.pending;

import com.alibaba.nacos.shaded.com.google.common.base.Throwables;
import com.zz.messagepush.cron.constant.PendingConstant;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.util.Lists;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @Description
 * @Author 张卫刚
 * @Date Created on 2023/4/6
 */
@Data
@Slf4j
public class BatchPendingThread<T> extends Thread {


    private PendingParam<T> pendingParam;

    /**
     * 批量装在任务
     */
    private List<T> list = new ArrayList<>();

    /**
     * 当前装载任务的大小
     */
    private Integer total = 0;

    /**
     * 上次执行的时间
     */
    private Long lastHandleTime = System.currentTimeMillis();

    @Override
    public void run() {
        while (true) {
            try {
                T poll = pendingParam.getBlockingQueue().poll(pendingParam.getThresholdTime(), TimeUnit.MILLISECONDS);
                if (poll != null) {
                    list.add(poll);
                }
                //处理条件: 1.数量超限  2.时间超限
                if (list.size()> PendingConstant.NUM_THRESHOLD || (System.currentTimeMillis()-lastHandleTime)>PendingConstant.TIME_THRESHOLD){
                    List<T> taskList = list;
                    list = new ArrayList<>();
                    lastHandleTime = System.currentTimeMillis();
                    pendingParam.getPending().handle(taskList);
                }
            } catch (Exception e) {
                log.error("BatchPendingThread#pending fail:{}", Throwables.getStackTraceAsString(e));
            }
        }
    }
}
