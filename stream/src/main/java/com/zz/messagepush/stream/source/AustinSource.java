package com.zz.messagepush.stream.source;

import com.zz.messagepush.common.domain.AnchorInfo;
import com.zz.messagepush.common.enums.AnchorStateEnum;
import org.apache.flink.streaming.api.functions.source.RichSourceFunction;

import java.util.ArrayList;
import java.util.List;

/**
 * @Description 数据源 mock
 * @Author 张卫刚
 * @Date Created on 2023/4/7
 */
public class AustinSource extends RichSourceFunction<AnchorInfo> {


    @Override
    public void run(SourceContext<AnchorInfo> sourceContext) {
        List<AnchorInfo> list = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            AnchorInfo anchorInfo = AnchorInfo.builder().state(AnchorStateEnum.RECEIVE.getCode())
                    .businessId(333L).timeStamp(System.currentTimeMillis()).build();
            list.add(anchorInfo);
        }

        for (AnchorInfo anchor : list) {
            sourceContext.collect(anchor);
        }
    }

    @Override
    public void cancel() {

    }
}
