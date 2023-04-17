package com.zz.messagepush.handler.shield;

import cn.hutool.core.date.DateUtil;
import com.alibaba.fastjson.JSON;
import com.zz.messagepush.common.domain.dto.TaskInfo;
import com.zz.messagepush.common.enums.ShieldType;
import com.zz.messagepush.common.utils.EnumUtil;
import com.zz.messagepush.support.utils.RedisUtil;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashSet;

/**
 * @Description
 * @Author 张卫刚
 * @Date Created on 2023/4/14
 */

@Service
public class ShieldServiceImpl implements ShieldService {


    private static final String NIGHT_SHIELD_BUT_NEXT_DAY_SEND_KEY = "night_shield_send";

    @Autowired
    private RedisUtil redisUtil;


    /**
     * example:当消息下发至austin平台时，已经是凌晨1点，业务希望此类消息在次日的早上9点推送
     * @param taskInfo
     */
    @Override
    public void shield(TaskInfo taskInfo) {
        if (isNight() && isNightShieldType(taskInfo.getShieldType())) {
            if (ShieldType.NIGHT_SHIELD_BUT_NEXT_DAY_SEND.getCode().equals(taskInfo.getIsNightShield())) {
                redisUtil.lPush(NIGHT_SHIELD_BUT_NEXT_DAY_SEND_KEY, JSON.toJSONString(taskInfo), (DateUtil.offsetDay(new Date(), 1).getTime()) / 1000);
            }
        }
        taskInfo.setReceiver(new HashSet<>());
    }


    /**
     * 根据code判断是否是夜间屏蔽类型
     *
     * @param code
     * @return
     */
    private boolean isNightShieldType(Integer code) {
        if (ShieldType.NIGHT_SHIELD.getCode().equals(code) || ShieldType.NIGHT_SHIELD_BUT_NEXT_DAY_SEND.getCode().equals(code)) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 是否凌晨 <8
     *
     * @return
     */
    private boolean isNight() {
        return Integer.parseInt(DateFormatUtils.format(new Date(), "HH")) < 8;
    }
}
