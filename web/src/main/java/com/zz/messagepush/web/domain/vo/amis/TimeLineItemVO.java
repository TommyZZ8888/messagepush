package com.zz.messagepush.web.domain.vo.amis;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @Description  时间线 timeline
 * @Author 张卫刚
 * @Date Created on 2023/4/11
 */


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TimeLineItemVO {

    private List<ItemVO> itemVOList;


    @Data
    @Builder
    public static class ItemVO {

        private String time;

        private String title;

        private String detail;

        private String icon;

        private String color;
    }

}
