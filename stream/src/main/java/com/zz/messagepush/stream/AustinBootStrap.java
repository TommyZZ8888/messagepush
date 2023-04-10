package com.zz.messagepush.stream;

import com.zz.messagepush.common.domain.AnchorInfo;
import com.zz.messagepush.stream.constant.AustinFlinkConstant;
import com.zz.messagepush.stream.function.AustinFlatMapFunction;
import com.zz.messagepush.stream.sink.AustinSink;
import com.zz.messagepush.stream.utils.MessageQueueUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.flink.api.common.eventtime.WatermarkStrategy;
import org.apache.flink.connector.kafka.source.KafkaSource;
import org.apache.flink.streaming.api.datastream.DataStreamSink;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

/**
 * @Description flink启动类
 * @Author 张卫刚
 * @Date Created on 2023/4/7
 */
@Slf4j
public class AustinBootStrap {

    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment executionEnvironment = StreamExecutionEnvironment.getExecutionEnvironment();

        //获取kafkaConsumer
        KafkaSource<String> kafkaConsumer = MessageQueueUtils.getKafkaConsumer(AustinFlinkConstant.TOPIC_NAME, AustinFlinkConstant.GROUP_ID, AustinFlinkConstant.BROKEN);
        DataStreamSource<String> kafkaSource = executionEnvironment.fromSource(kafkaConsumer, WatermarkStrategy.noWatermarks(), "kafkaSource");
        //数据转换的处理
        SingleOutputStreamOperator<AnchorInfo> dataStream = kafkaSource.flatMap(new AustinFlatMapFunction()).name(AustinFlinkConstant.FUNCTION_NAME);

        dataStream.addSink(new AustinSink()).name(AustinFlinkConstant.SINK_NAME);
        executionEnvironment.execute(AustinFlinkConstant.JOB_NAME);
    }
}
