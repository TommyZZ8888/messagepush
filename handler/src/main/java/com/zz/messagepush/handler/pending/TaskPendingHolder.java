package com.zz.messagepush.handler.pending;

import com.zz.messagepush.handler.config.ThreadPoolConfig;
import com.zz.messagepush.handler.utils.GroupIdMappingUtils;
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

    private Map<String, ExecutorService> taskPendingGolder = new HashMap<>();

    private List<String> groupIds = GroupIdMappingUtils.getAllGroupIds();


    /**
     * 给每个渠道，每种消息类型初始化一个线程池
     * TODO 不同的groupId 分配不同的线程和队列大小
     */
    @PostConstruct
    public void init() {
        for (String groupId : groupIds) {
            /**
             * 线程池参数
             */
            Integer coreSize = 5;
            Integer queueSize = 1000;
            Integer maxSize = 50;
            taskPendingGolder.put(groupId, ThreadPoolConfig.getThreadPool(coreSize, maxSize, queueSize));
        }
    }

    /**
     * 得到对应的线程池
     *
     * @param groupId
     * @return
     */
    public ExecutorService route(String groupId) {
        return taskPendingGolder.get(groupId);
    }

}
