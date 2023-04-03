package com.zz.messagepush.common.utils;


import com.zz.messagepush.common.domain.PageParam;
import com.zz.messagepush.common.domain.PageResult;
import org.apache.commons.collections4.ListUtils;

import javax.crypto.spec.OAEPParameterSpec;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class PageUtil {

//    public static <T> PageResult<T> convert2PageResult(IPage<T> page) {
//        PageResult<T> pageResult = new PageResult<>();
//        pageResult.setPage(page.getCurrent());
//        pageResult.setPageSize(page.getSize());
//        pageResult.setTotal(page.getTotal());
//        pageResult.setPages(page.getPages());
//        pageResult.setList(page.getRecords());
//        return pageResult;
//    }
//
//    public static <T> PageResult<T> convert2PageResult(List<T> sourceList, PageParam dto) {
//        List<List<T>> partition = ListUtils.partition(sourceList, dto.getPageSize());
//        PageResult<T> pageResult = new PageResult<>();
//        pageResult.setPage((long) dto.getPageIndex());
//        pageResult.setPages((long) partition.size());
//        pageResult.setTotal((long) sourceList.size());
//        pageResult.setPageSize((long) dto.getPageSize());
//
//        if (dto.getPageIndex() > partition.size()) {
//            pageResult.setList(new ArrayList<>());
//        } else {
//            pageResult.setList(partition.get(dto.getPageIndex() - 1));
//        }
//        return pageResult;
//    }
//
//
//    public static <T> Page<T> convert2QueryPage(PageParam baseDTO) {
//        Page<T> page = new Page<>();
//        List<com.zz.messagepush.common.domain.OrderItem> orderItemList = baseDTO.getOrderItemList();
//        List<OrderItem> collect = Optional.of(orderItemList).orElse(new ArrayList<>()).stream().map(PageUtil::convertOrderItem).collect(Collectors.toList());
//        page.setOrders(collect);
//        page.setSize(baseDTO.getPageSize());
//        page.setCurrent(baseDTO.getPageIndex());
//        if (null != baseDTO.getSearchCount()) {
//            page.setSearchCount(baseDTO.getSearchCount());
//        }
//        return page;
//
//
//    }
//
//
//    public static OrderItem convertOrderItem(com.zz.messagepush.common.domain.OrderItem orderItemDto) {
//        if (orderItemDto.isAsc()) {
//            return OrderItem.asc(orderItemDto.getColumn());
//        } else {
//            return OrderItem.desc(orderItemDto.getColumn());
//        }
//    }
//
//
//    public static <T, E> PageResult<T> convert2PageResult(IPage page, List<E> sourceList, Class<T> targetClazz) {
//        PageResult<T> pageResult = setPage(page);
//        List<T> records = BeanUtil.copyList(sourceList, targetClazz);
//        page.setRecords(records);
//        pageResult.setList(records);
//        return pageResult;
//    }
//
//    /**
//     * 转换为 PageResultDTO 对象
//     *
//     * @param page
//     * @param sourceList list
//     * @return
//     */
//    public static <T, E> PageResult<T> convert2PageResult(IPage page, List<E> sourceList) {
//        PageResult pageResult = setPage(page);
//        page.setRecords(sourceList);
//        pageResult.setList(sourceList);
//        return pageResult;
//    }
//
//    private static PageResult setPage(IPage page) {
//        PageResult pageResult = new PageResult();
//        pageResult.setPage(page.getCurrent());
//        pageResult.setPageSize(page.getSize());
//        pageResult.setTotal(page.getTotal());
//        pageResult.setPages(page.getPages());
//        return pageResult;
//    }
}
