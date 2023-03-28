package com.zz.messagepush.web.domain.vo;

import com.zz.messagepush.support.domain.entity.MessageTemplateEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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
    private Iterable<MessageTemplateEntity> rows;

    /**
     * 总条数
     */
    private Long count;

}
