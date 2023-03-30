package com.zz.messagepush.handler;

import com.zz.messagepush.common.domain.dto.TaskInfo;
import com.zz.messagepush.handler.deduplication.DeduplicationRuleService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

/**
 * @Description
 * @Author 张卫刚
 * @Date Created on 2023/3/29
 */

@SpringBootTest
public class HandlerTest {

    @Resource
    private DeduplicationRuleService deduplicationRuleService;


    @Test
    void context() {
        deduplicationRuleService.duplication(new TaskInfo());
    }
}
