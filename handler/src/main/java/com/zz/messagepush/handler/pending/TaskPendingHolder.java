package com.zz.messagepush.handler.pending;

import com.dtp.common.em.QueueTypeEnum;
import com.dtp.core.DtpRegistry;
import com.dtp.core.thread.DtpExecutor;
import com.dtp.core.thread.ThreadPoolBuilder;
import com.zz.messagepush.handler.config.ThreadPoolConfig;
import com.zz.messagepush.handler.utils.GroupIdMappingUtils;
import com.zz.messagepush.support.config.ThreadPoolExecutorShutdownDefinition;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @Description 存储每种消息类型与TaskPending的关系
 * @Author 张卫刚
 * @Date Created on 2023/3/22
 */

@Component
public class TaskPendingHolder {

    private Map<String, ExecutorService> taskPendingHolder = new HashMap<>();

    private List<String> groupIds = GroupIdMappingUtils.getAllGroupIds();

    @Autowired
    private ThreadPoolExecutorShutdownDefinition threadPoolExecutorShutdownDefinition;



    @Autowired
    private DtpRegistry dtpRegistry;

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

            DtpExecutor dtpExecutor = ThreadPoolBuilder.newBuilder()
                    .threadPoolName("austin-" + groupId)
                    .corePoolSize(10)
                    .maximumPoolSize(15)
                    .keepAliveTime(15000)
                    .timeUnit(TimeUnit.MILLISECONDS)
                    .workQueue(QueueTypeEnum.SYNCHRONOUS_QUEUE.getName(), null, false)
                    .buildDynamic();
            DtpRegistry.register(dtpExecutor,"beanPostProcessor");
            threadPoolExecutorShutdownDefinition.registryExecutor(dtpExecutor);
            taskPendingHolder.put(groupId, dtpExecutor);
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
