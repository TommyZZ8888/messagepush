package com.zz.messagepush.handler.deduplication.handler;

import com.alibaba.fastjson.JSONObject;
import com.zz.messagepush.common.enums.AnchorStateEnum;
import com.zz.messagepush.handler.domain.DeduplicationParam;
import org.springframework.stereotype.Service;

/**
 * @Description
 * @Author 张卫刚
 * @Date Created on 2023/3/28
 */
@Service
public class ContentDeduplicationBuilder implements Builder{
    public DeduplicationParam build(String deduplication, String key) {
        JSONObject object = JSONObject.parseObject(deduplication);
        if (object == null) {
            return null;
        }
        DeduplicationParam deduplicationParam = JSONObject.parseObject(object.getString(key), DeduplicationParam.class);
        if (deduplicationParam == null) {
            return null;
        }
        deduplicationParam.setAnchorStateEnum(AnchorStateEnum.RULE_DEDUPLICATION);
        return deduplicationParam;

    }
}
