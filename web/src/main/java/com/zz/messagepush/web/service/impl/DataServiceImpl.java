package com.zz.messagepush.web.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.text.StrPool;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONObject;
import com.zz.messagepush.common.constant.AustinConstant;
import com.zz.messagepush.common.enums.AnchorStateEnum;
import com.zz.messagepush.common.enums.ChannelType;
import com.zz.messagepush.common.enums.SimpleAnchorInfo;
import com.zz.messagepush.support.domain.entity.MessageTemplateEntity;
import com.zz.messagepush.support.mapper.MessageTemplateMapper;
import com.zz.messagepush.support.utils.RedisUtil;
import com.zz.messagepush.support.utils.TaskInfoUtils;
import com.zz.messagepush.web.constant.AmisVoConstant;
import com.zz.messagepush.web.domain.vo.amis.EchartsVO;
import com.zz.messagepush.web.domain.vo.amis.UserTimeLineVO;
import com.zz.messagepush.web.service.DataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
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
    public UserTimeLineVO getTraceUserInfo(String receiver) {
        List<String> userInfoList = redisUtil.lRange(receiver, 0L, (long) -1);
        if (CollUtil.isEmpty(userInfoList)) {
            return UserTimeLineVO.builder().itemVOList(new ArrayList<>()).build();
        }
        //按时间排序
        List<SimpleAnchorInfo> sortAnchorList = userInfoList.stream().map(s -> JSONObject.parseObject(s, SimpleAnchorInfo.class))
                .sorted((o1, o2) -> Math.toIntExact(o1.getTimestamp() - o2.getTimestamp())).collect(Collectors.toList());

        //对相同的businessId进行分类
        Map<Long, List<SimpleAnchorInfo>> map = sortAnchorList.stream().collect(Collectors.groupingBy(SimpleAnchorInfo::getBusinessId, Collectors.toList()));

        //封装vo给前端渲染展示
        List<UserTimeLineVO.ItemVO> itemVOList = new ArrayList<>();
        map.entrySet().stream().peek(item -> {
            Long key = item.getKey();
            Long businessId = TaskInfoUtils.getDateFromBusinessId(String.valueOf(key));
            MessageTemplateEntity messageTemplateEntity = messageTemplateMapper.findById(businessId).orElse(null);
            if (messageTemplateEntity == null) {
                return;
            }

            StringBuilder sb = new StringBuilder();
            for (SimpleAnchorInfo simpleAnchorInfo : item.getValue()) {
                if (AnchorStateEnum.RECEIVE.getCode().equals(simpleAnchorInfo.getState())) {
                    sb.append(StrPool.CRLF);
                }
                String startTime = DateUtil.format(new Date(simpleAnchorInfo.getTimestamp()), DatePattern.NORM_DATETIME_PATTERN);
                String stateDescription = AnchorStateEnum.getDescriptionByCode(simpleAnchorInfo.getState());
                sb.append(startTime).append(StrPool.C_COLON).append(stateDescription).append("==>");
            }
            ChannelType enumByCode = ChannelType.getEnumByCode(messageTemplateEntity.getSendChannel());
            String description = enumByCode == null ? null : enumByCode.getDescription();
            for (String detail : sb.toString().split(StrPool.CRLF)) {
                if (StrUtil.isNotBlank(detail)) {
                    UserTimeLineVO.ItemVO itemsVO = UserTimeLineVO.ItemVO.builder()
                            .businessId(String.valueOf(item.getKey()))
                            .sendType(description)
                            .creator(messageTemplateEntity.getCreator())
                            .title(messageTemplateEntity.getName())
                            .detail(detail)
                            .build();
                    itemVOList.add(itemsVO);
                }
            }
        }).collect(Collectors.toList());

        return UserTimeLineVO.builder().itemVOList(itemVOList).build();
    }

    @Override
    public EchartsVO getTraceMessageTemplateInfo(String businessId) {
        String realBusinessId = getRealBusinessId(businessId);
        Optional<MessageTemplateEntity> templateEntity = messageTemplateMapper.findById(TaskInfoUtils.getMessageTemplateIdFromBusinessId(Long.parseLong(businessId)));
        if (templateEntity.isEmpty()) {
            return null;
        }
        MessageTemplateEntity messageTemplateEntity = templateEntity.get();
        List<String> xAxisList = new ArrayList<>();
        List<Integer> actualData = new ArrayList<>();

        Map<Object, Object> anchorResult = redisUtil.hGetAll(realBusinessId);
        if (CollUtil.isNotEmpty(anchorResult)) {
            anchorResult = MapUtil.sort(anchorResult);
            for (Map.Entry<Object, Object> entry : anchorResult.entrySet()) {
                String description = AnchorStateEnum.getDescriptionByCode(Integer.valueOf(String.valueOf(entry.getKey())));
                xAxisList.add(description);
                actualData.add(Integer.valueOf(String.valueOf(entry.getValue())));
            }
        }
        String title = "【" + messageTemplateEntity.getName() + "】在" + TaskInfoUtils.getDateFromBusinessId(businessId) + "的下发情况：";

        return EchartsVO.builder()
                .title(EchartsVO.TitleVO.builder().text(title).build())
                .legend(EchartsVO.LegendVO.builder().data(List.of(AmisVoConstant.LEGEND_TITLE)).build())
                .xAxis(EchartsVO.XAxisVO.builder().data(xAxisList).build())
                .series(List.of(EchartsVO.SeriesVO.builder().name(AmisVoConstant.LEGEND_TITLE).type(AmisVoConstant.TYPE).data(actualData).build()))
                .yAxis(EchartsVO.YAxisVO.builder().build())
                .tooltip(EchartsVO.ToolTipVO.builder().build())
                .build();
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
