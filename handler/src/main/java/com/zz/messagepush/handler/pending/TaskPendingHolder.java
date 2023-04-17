package com.zz.messagepush.handler.pending;

import com.dtp.core.thread.DtpExecutor;
import com.zz.messagepush.handler.config.HandlerThreadPoolConfig;
import com.zz.messagepush.handler.utils.GroupIdMappingUtils;
import com.zz.messagepush.support.utils.ThreadPoolUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;

/**
 * @Description 存储每种消息类型与TaskPending的关系
 * @Author 张卫刚
 * @Date Created on 2023/3/22
 */

@Component
public class TaskPendingHolder {

    private final Map<String, ExecutorService> taskPendingHolder = new HashMap<>();

    private final List<String> groupIds = GroupIdMappingUtils.getAllGroupIds();

    @Autowired
    private ThreadPoolUtils threadPoolUtils;


    /**
     * 给每个渠道，每种消息类型初始化一个线程池
     * TODO 不同的groupId 分配不同的线程和队列大小
     */
    @PostConstruct
    public void init() {
        for (String groupId : groupIds) {
            DtpExecutor executor = HandlerThreadPoolConfig.getExecutor(groupId);
            threadPoolUtils.registry(executor);
            taskPendingHolder.put(groupId, executor);
        }
    }

    /**
     * 得到对应的线程池
     *
     * @param groupId
     * @return
     */
    public ExecutorService route(String groupId) {
        return taskPendingHolder.get(groupId);
    }

}
