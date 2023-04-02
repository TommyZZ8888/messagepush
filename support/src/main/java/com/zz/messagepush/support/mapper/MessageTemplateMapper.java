package com.zz.messagepush.support.mapper;

import com.zz.messagepush.support.domain.entity.MessageTemplateEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
* @author DELL
* @description 针对表【message_template(消息模板信息)】的数据库操作Mapper
* @createDate 2023-03-08 15:21:05
* @Entity com.zz.messagepush.support.domain.entity.MessageTemplate
*/

public interface MessageTemplateMapper extends JpaRepository<MessageTemplateEntity,Long> {

    /**
     * 查询列表 分页
     * @param deleted
     * @param pageable
     * @return
     */
    List<MessageTemplateEntity> findAllByIsDeletedEquals(Integer deleted, Pageable pageable);

    /**
     * 统计未删除的条数
     * @param deleted
     * @return
     */
    Long countByIdDeletedEquals(Integer deleted);
}
