package com.zz.messagepush.handler.receipt;

import com.zz.messagepush.handler.receipt.starter.ReceiptMessageStarter;
import com.zz.messagepush.support.config.SupportThreadPoolConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.List;

/**
 * @Description SmsReceipt
 * @Author 张卫刚
 * @Date Created on 2023/5/4
 */

@Component
@Slf4j
public class MessageReceipt {

    @Autowired
    private List<ReceiptMessageStarter> receiptMessageStarterList;


    @PostConstruct
    private void init() {
        SupportThreadPoolConfig.getPendingSingleThreadPool().execute(() -> {
            while (true) {
                //TODO 回执这里自行打开，免得报错
                for (ReceiptMessageStarter messageStarter : receiptMessageStarterList) {
//                    messageStarter.start();
                }
                try {
                    Thread.sleep(200);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
