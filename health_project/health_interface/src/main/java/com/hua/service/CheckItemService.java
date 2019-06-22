package com.hua.service;

import com.hua.entity.PageResult;
import com.hua.pojo.CheckItem;

import java.util.List;

public interface CheckItemService {
    /**
     * 分页查询
     * @param currentPage
     * @param pageSize
     * @param queryString
     * @return
     */
    PageResult findPage(Integer currentPage,Integer pageSize,String queryString);

    /**
     *
     * 添加检查项
     * @param checkItem
     */
    void add(CheckItem checkItem);

    /**
     * 根据id删除检查项
     * @param id
     */
    void delById(Integer id);

    /**
     * 数据回显
     * @param id
     * @return
     */
    CheckItem findById(Integer id);

    /**
     * 编辑检查项
     * @param checkItem
     */
    void update(CheckItem checkItem);

    /**
     * 获取所有检查项
     * @return
     */
    List<CheckItem> findAll();
}
