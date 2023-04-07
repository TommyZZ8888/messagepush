package com.zz.messagepush.stream;

import com.alibaba.fastjson.JSON;
import com.qiniu.util.Json;
import com.zz.messagepush.common.utils.ApplicationContextUtil;
import com.zz.messagepush.stream.utils.FlinkUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.flink.api.common.eventtime.WatermarkStrategy;
import org.apache.flink.connector.kafka.source.KafkaSource;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.sink.SinkFunction;

/**
 * @Description flink启动类
 * @Author 张卫刚
 * @Date Created on 2023/4/7
 */
@Slf4j
public class AustinBootStrap {

    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment executionEnvironment = StreamExecutionEnvironment.getExecutionEnvironment();

        String topicName = "austinTopicV2";
        String groupId = "austinTopicV3";
        KafkaSource<String> kafkaConsumer = ApplicationContextUtil.getBean(FlinkUtils.class).getKafkaConsumer(topicName, groupId);

        DataStreamSource<String> kafkaSource = executionEnvironment.fromSource(kafkaConsumer, WatermarkStrategy.noWatermarks(), "kafkaSource");

        kafkaSource.addSink(new SinkFunction<String>() {
            @Override
            public void invoke(String value, Context context) {
                log.info("kafka value:{}", value);
            }
        });
        executionEnvironment.execute("AustinBootStrap");
    }
}
