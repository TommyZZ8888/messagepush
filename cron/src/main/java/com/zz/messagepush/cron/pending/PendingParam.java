package com.zz.messagepush.cron.pending;

import lombok.Data;

import java.util.concurrent.BlockingQueue;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * @Description
 * @Author 张卫刚
 * @Date Created on 2023/4/6
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class PendingParam<T> {

    private BlockingQueue<T> blockingQueue;

    private Integer thresholdNum;

    private Long thresholdTime;

    private Integer threadNum;

    private Pending<T> pending;
}
