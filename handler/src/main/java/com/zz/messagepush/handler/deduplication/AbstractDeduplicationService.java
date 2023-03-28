package com.zz.messagepush.handler.deduplication;

import cn.hutool.core.collection.CollUtil;
import com.zz.messagepush.common.constant.AustinConstant;
import com.zz.messagepush.common.domain.AnchorInfo;
import com.zz.messagepush.common.domain.dto.TaskInfo;
import com.zz.messagepush.handler.deduplication.handler.DeduplicationService;
import com.zz.messagepush.handler.domain.DeduplicationParam;
import com.zz.messagepush.support.utils.LogUtils;
import com.zz.messagepush.support.utils.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;
import java.util.*;

/**
 * @Description 去重服务
 * @Author 张卫刚
 * @Date Created on 2023/3/23
 */


public abstract class AbstractDeduplicationService implements DeduplicationService {


    protected Integer deduplicationType;

    @Autowired
    private DeduplicationHolder deduplicationHolder;


    @PostConstruct
    public void init() {
        deduplicationHolder.putService(deduplicationType, this);
    }

    @Autowired
    private RedisUtil redisUtil;


    @Override
    public void deduplication(DeduplicationParam deduplicationParam) {
        TaskInfo taskInfo = deduplicationParam.getTaskInfo();
        Set<String> filterReceiver = new HashSet<>(taskInfo.getReceiver().size());

        //获取redis记录
        Set<String> readyPutRedisReceiver = new HashSet<>(taskInfo.getReceiver().size());
        List<String> keys = deduplicationAllKey(taskInfo);
        Map<String, String> inRedisValue = redisUtil.mGet(keys);

        for (String receiver : taskInfo.getReceiver()) {
            String key = deduplicationSingleKey(taskInfo, receiver);
            String value = inRedisValue.get(key);

            if (value != null && Integer.parseInt(value) >= deduplicationParam.getCountNum()) {
                filterReceiver.add(receiver);
            } else {
                readyPutRedisReceiver.add(receiver);
            }
        }
        //不符合条件的用户，需要更新redis（无记录添加，有记录累加次数）
        putRedis(readyPutRedisReceiver, inRedisValue, deduplicationParam);

        //剔除符合去重条件的用户
        if (CollUtil.isNotEmpty(filterReceiver)) {
            taskInfo.getReceiver().removeAll(filterReceiver);
            LogUtils.print(AnchorInfo.builder().businessId(taskInfo.getBusinessId()).ids(filterReceiver).state(deduplicationParam.getAnchorStateEnum().getCode()).build());
        }
    }


    /**
     * 构建去重key
     *
     * @param taskInfo
     * @param receiver
     * @return
     */
    protected abstract String deduplicationSingleKey(TaskInfo taskInfo, String receiver);


    /**
     * 存入redis  实现去重
     *
     * @param readyPutRedisReceiver
     * @param inRedisValue
     * @param param
     */
    private void putRedis(Set<String> readyPutRedisReceiver, Map<String, String> inRedisValue, DeduplicationParam param) {
        Map<String, String> keyValues = new HashMap<>(readyPutRedisReceiver.size());
        for (String receiver : readyPutRedisReceiver) {
            String singleKey = deduplicationSingleKey(param.getTaskInfo(), receiver);
            if (inRedisValue.get(singleKey) != null) {
                keyValues.put(singleKey, String.valueOf(Integer.parseInt(inRedisValue.get(singleKey)) + 1));
            } else {
                keyValues.put(singleKey, String.valueOf(AustinConstant.TRUE));
            }
            if (CollUtil.isNotEmpty(keyValues)) {
                redisUtil.pipelineSetEx(keyValues, param.getDeduplicationTime());
            }
        }
    }


    /**
     * 获取当前信息模板所有的去重key
     *
     * @param taskInfo
     * @return
     */
    public List<String> deduplicationAllKey(TaskInfo taskInfo) {
        List<String> result = new ArrayList<>(taskInfo.getReceiver().size());

        for (String receiver : taskInfo.getReceiver()) {
            String singleKey = deduplicationSingleKey(taskInfo, receiver);
            result.add(singleKey);
        }
        return result;
    }
}
