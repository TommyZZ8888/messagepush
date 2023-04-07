package com.zz.messagepush.stream.sink;

import com.alibaba.fastjson.JSON;
import com.zz.messagepush.common.domain.AnchorInfo;
import lombok.extern.slf4j.Slf4j;
import org.apache.flink.streaming.api.functions.sink.RichSinkFunction;

/**
 * @Description
 * @Author 张卫刚
 * @Date Created on 2023/4/7
 */
@Slf4j
public class AustinSink extends RichSinkFunction<AnchorInfo> {

    @Override
    public void invoke(AnchorInfo value, Context context) throws Exception {
      log.info("sink consume value:{}", JSON.toJSONString(value));
    }
}
