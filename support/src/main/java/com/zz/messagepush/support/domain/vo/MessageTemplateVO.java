package com.zz.messagepush.support.domain.vo;

import com.zz.messagepush.support.domain.entity.MessageTemplateEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

/**
 * @Description
 * @Author 张卫刚
 * @Date Created on 2023/3/28
 */

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MessageTemplateVO {


    /**
     * 消息模板
     */
    private List<Map<String,Object>> rows;

    /**
     * 总条数
     */
    private Long count;

}
