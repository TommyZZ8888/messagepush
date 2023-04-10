package com.zz.messagepush.stream.function;

import com.alibaba.fastjson.JSON;
import com.zz.messagepush.common.domain.AnchorInfo;
import org.apache.flink.api.common.functions.FlatMapFunction;
import org.apache.flink.util.Collector;

/**
 * @Description process 处理
 * @Author 张卫刚
 * @Date Created on 2023/4/10
 */

public class AustinFlatMapFunction implements FlatMapFunction<String, AnchorInfo> {


    @Override
    public void flatMap(String value, Collector<AnchorInfo> collector) throws Exception {
        AnchorInfo anchorInfo = JSON.parseObject(value, AnchorInfo.class);
        collector.collect(anchorInfo);
    }
}
