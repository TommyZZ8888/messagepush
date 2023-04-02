package com.zz.messagepush.support.service;

import com.github.yulichang.base.MPJBaseMapper;
import com.zz.messagepush.common.domain.PageResult;
import com.zz.messagepush.support.domain.dto.MessageTemplateParamDTO;
import com.zz.messagepush.support.domain.entity.MessageTemplateEntity;

import java.util.List;

/**
 * @author DELL
 * @description 针对表【message_template(消息模板信息)】的数据库操作Service
 * @createDate 2023-03-08 15:21:05
 */
public interface MessageTemplateService {

    /**
     * 查询未删除的模板列表（分页）
     *
     * @param paramDTO
     * @return
     */
    List<MessageTemplateEntity> queryNotDeletedList(MessageTemplateParamDTO paramDTO);


    /**
     * 统计未删除的条数
     *
     * @return
     */
    Long notDeletedCount();

    /**
     * 存在id更新
     * 不存在id保存
     *
     * @param messageTemplateParamDTO
     */
    void saveOrUpdate(MessageTemplateParamDTO messageTemplateParamDTO);


    /**
     * 软删除(deleted=1)
     *
     * @param ids
     */
    void deleteByIds(List<Long> ids);

    /**
     * 根据id查询模板信息
     *
     * @param id
     * @return
     */
    MessageTemplateEntity queryById(Long id);

    /**
     * 复制配置
     */
    void copy(Long id);

    /**
     * 启动模板的定时任务
     */
    void startCronTask(Long id);

    /**
     * 暂停模板的定时任务
     */
    void stopCronTask(Long id);
}
