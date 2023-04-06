package com.zz.messagepush.cron.pending;

import com.zz.messagepush.cron.domain.vo.CrowdInfoVO;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @Description
 * @Author 张卫刚
 * @Date Created on 2023/4/6
 */
@Component
public class CrowdBatchTaskPending extends Pending<CrowdInfoVO> {
    @Override
    public void initAndStart(PendingParam<CrowdInfoVO> pendingParam) {
        threadNum = pendingParam.getThreadNum() == null ? threadNum : pendingParam.getThreadNum();
        blockingQueue = pendingParam.getBlockingQueue();
        for (int i = 0; i < threadNum; i++) {
            BatchPendingThread<CrowdInfoVO> batchPendingThread = new BatchPendingThread<>();
            batchPendingThread.setPendingParam(pendingParam);
            batchPendingThread.setName("batchPendingThread-" + i);
            batchPendingThread.start();
        }
    }

    @Override
    public void doHandle(List<CrowdInfoVO> list) {

    }
}
