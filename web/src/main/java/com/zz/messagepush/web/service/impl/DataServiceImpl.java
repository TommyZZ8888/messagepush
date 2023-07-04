package com.zz.messagepush.web.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.text.StrPool;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONObject;
import com.zz.messagepush.common.constant.AustinConstant;
import com.zz.messagepush.common.enums.*;
import com.zz.messagepush.common.utils.EnumUtil;
import com.zz.messagepush.support.domain.entity.MessageTemplateEntity;
import com.zz.messagepush.support.domain.entity.SmsRecordEntity;
import com.zz.messagepush.support.mapper.MessageTemplateMapper;
import com.zz.messagepush.support.mapper.SmsRecordMapper;
import com.zz.messagepush.support.utils.RedisUtil;
import com.zz.messagepush.support.utils.TaskInfoUtils;
import com.zz.messagepush.web.constant.AmisVoConstant;
import com.zz.messagepush.web.domain.vo.DataParamVO;
import com.zz.messagepush.web.domain.vo.amis.EchartsVO;
import com.zz.messagepush.web.domain.vo.amis.SmsTimeLineVO;
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

    @Autowired
    private SmsRecordMapper smsRecordMapper;


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
                String stateDescription = EnumUtils.getDescriptionByCode(simpleAnchorInfo.getState(),AnchorStateEnum.class);
                sb.append(startTime).append(StrPool.C_COLON).append(stateDescription).append("==>");
            }
            ChannelType enumByCode = EnumUtils.getEnumByCode(messageTemplateEntity.getSendChannel(),ChannelType.class);
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
                String description = EnumUtils.getDescriptionByCode(Integer.valueOf(String.valueOf(entry.getKey())),AnchorStateEnum.class);
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

    @Override
    public SmsTimeLineVO getTraceSmsInfo(DataParamVO dataParamVO) {
        List<SmsTimeLineVO.ItemsVO> itemsVOS = new ArrayList<>();
        SmsTimeLineVO smsTimeLineVO = SmsTimeLineVO.builder().items(itemsVOS).build();
        Integer sendDate = Integer.valueOf(DateUtil.format(new Date(dataParamVO.getDateTime() * 1000L), DatePattern.PURE_DATE_PATTERN));
        List<SmsRecordEntity> smsRecordEntityList = smsRecordMapper.findByPhoneAndSendDate(Long.valueOf(String.valueOf(dataParamVO.getReceiver())), sendDate);
        if (CollUtil.isEmpty(smsRecordEntityList)) {
            return smsTimeLineVO;
        }

        Map<String, List<SmsRecordEntity>> map = smsRecordEntityList.stream().collect(Collectors.groupingBy(o -> o.getPhone() + o.getSeriesId()));
        for (Map.Entry<String, List<SmsRecordEntity>> entry : map.entrySet()) {
            SmsTimeLineVO.ItemsVO itemsVO = SmsTimeLineVO.ItemsVO.builder().build();
            for (SmsRecordEntity smsRecord : entry.getValue()) {
                // 发送记录 messageTemplateId >0 ,回执记录 messageTemplateId =0
                if (smsRecord.getMessageTemplateId() > 0) {
                    itemsVO.setBusinessId(String.valueOf(smsRecord.getMessageTemplateId()));
                    itemsVO.setContent(smsRecord.getMsgContent());
                    itemsVO.setSendType(EnumUtil.getValue(SmsStatus.class, smsRecord.getStatus()));
                    itemsVO.setSendTime(DateUtil.format(new Date(smsRecord.getCreated().getTime() * 1000L), DatePattern.NORM_DATETIME_PATTERN));

                } else {
                    itemsVO.setReceiveType(EnumUtil.getValue(SmsStatus.class, smsRecord.getStatus()));
                    itemsVO.setReceiveContent(smsRecord.getReportContent());
                    itemsVO.setReceiveTime(DateUtil.format(new Date(smsRecord.getUpdated().getTime() * 1000L), DatePattern.NORM_DATETIME_PATTERN));
                }
                itemsVOS.add(itemsVO);
            }
        }
        return smsTimeLineVO;
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
        if (Objects.nonNull(messageTemplateEntity)) {
            Long generateBusinessId = TaskInfoUtils.generateBusinessId(messageTemplateEntity.getId(), messageTemplateEntity.getTemplateType());
            return String.valueOf(generateBusinessId);
        }
        return businessId;
    }
}
