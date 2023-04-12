package com.zz.messagepush.stream.sink;

import cn.hutool.core.date.DateUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.nacos.shaded.com.google.common.base.Throwables;
import com.zz.messagepush.common.domain.AnchorInfo;
import com.zz.messagepush.common.enums.SimpleAnchorInfo;
import com.zz.messagepush.stream.utils.LettuceRedisUtils;
import io.lettuce.core.RedisFuture;
import lombok.extern.slf4j.Slf4j;
import org.apache.flink.streaming.api.functions.sink.SinkFunction;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @Description 消息进redis/hive
 * @Author 张卫刚
 * @Date Created on 2023/4/7
 */
@Slf4j
public class AustinSink implements SinkFunction<AnchorInfo> {

    @Override
    public void invoke(AnchorInfo anchorInfo, Context context) {
        realTimeData(anchorInfo);
        offlineData(anchorInfo);
    }

    /**
     * 实时存入redis
     * 1、用户维度（查看用户当天收到信息的链路详情），数据级大，只查看当天
     * 2、消息模板维度（查看消息模板整体下发情况），数据级小，保留30天
     */
    private void realTimeData(AnchorInfo anchorInfo) {

        try {
            LettuceRedisUtils.pipeline(redisAsyncCommands -> {
                List<RedisFuture<?>> redisFutures = new ArrayList<>();
                //构建用户id维度的链路数据list：{key：list}
                SimpleAnchorInfo simpleAnchorInfo = SimpleAnchorInfo.builder().businessId(anchorInfo.getBusinessId()).state(anchorInfo.getState()).build();
                for (String id : anchorInfo.getIds()) {
                    redisFutures.add(redisAsyncCommands.lpush(id.getBytes(), JSON.toJSONString(simpleAnchorInfo).getBytes()));
                    redisFutures.add(redisAsyncCommands.expire(id.getBytes(), (DateUtil.endOfDay(new Date()).getTime() - DateUtil.current()) / 1000));
                }

                //构建消息模板维度的链路数据 数据结构hash：{key，hash}
                redisFutures.add(redisAsyncCommands.hincrby(String.valueOf(anchorInfo.getBusinessId()).getBytes(), String.valueOf(anchorInfo.getState()).getBytes(), anchorInfo.getIds().size()));
                redisFutures.add(redisAsyncCommands.expire(String.valueOf(anchorInfo.getBusinessId()).getBytes(), (DateUtil.offsetDay(new Date(), 30).getTime() - DateUtil.current()) / 1000));
                return redisFutures;
            });
        } catch (Exception e) {
            log.error("austinsink realTimeData fail:{}", Throwables.getStackTraceAsString(e));
        }
    }


    /**
     * 离线数据存入hive
     */
    private void offlineData(AnchorInfo anchorInfo) {

    }
}
