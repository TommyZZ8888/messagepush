package com.zz.messagepush.web.service.impl;

import com.zz.messagepush.common.constant.AustinConstant;
import com.zz.messagepush.support.domain.entity.MessageTemplateEntity;
import com.zz.messagepush.support.mapper.MessageTemplateMapper;
import com.zz.messagepush.support.utils.RedisUtil;
import com.zz.messagepush.support.utils.TaskInfoUtils;
import com.zz.messagepush.web.domain.vo.amis.EchartsVO;
import com.zz.messagepush.web.domain.vo.amis.TimeLineItemVO;
import com.zz.messagepush.web.service.DataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @Description
 * @Author 张卫刚
 * @Date Created on 2023/4/11
 */
@Service
public class DataServiceImpl implements DataService {

    @Autowired
    private RedisUtil redisUtil;

    @Autowired
    private MessageTemplateMapper messageTemplateMapper;


    @Override
    public TimeLineItemVO getTraceUserInfo(String receiver) {
        return null;
    }

    @Override
    public EchartsVO getTraceMessageTemplateInfo(String businessId) {
        String realBusinessId = getRealBusinessId(businessId);
        Map<Object, Object> objectObjectMap = redisUtil.hGetAll(realBusinessId);
        List<Integer> collect = objectObjectMap.keySet().stream().map(o -> Integer.valueOf(String.valueOf(o))).collect(Collectors.toList());
   return null;
    }

    /**
     * 如果传入的是消息模板id，则生成当天的businessId
     * 如果传入的businessId，检查是否是十六位
     *
     * @param businessId
     * @return
     */
    public String getRealBusinessId(String businessId) {
        if (businessId.length() == AustinConstant.BUSINESS_ID_LENGTH) {
            return businessId;
        }
        MessageTemplateEntity messageTemplateEntity = messageTemplateMapper.findById(Long.parseLong(businessId)).orElse(null);
        if (messageTemplateEntity != null) {
            Long generateBusinessId = TaskInfoUtils.generateBusinessId(messageTemplateEntity.getId(), messageTemplateEntity.getTemplateType());
            return String.valueOf(generateBusinessId);
        }
        return businessId;
    }
}
