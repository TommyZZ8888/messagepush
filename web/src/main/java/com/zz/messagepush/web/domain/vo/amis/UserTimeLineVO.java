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
public class UserTimeLineVO {

    private List<ItemVO> itemVOList;


    @Data
    @Builder
    public static class ItemVO {

        /**
         * 业务id
         */
        private String businessId;

        /**
         * 模板名称
         */
        private String title;

        /**
         * 发送细节
         */
        private String detail;

        /**
         * 模板创建者
         */
        private String creator;

        /**
         * 发送类型
         */
        private String sendType;
    }

}
