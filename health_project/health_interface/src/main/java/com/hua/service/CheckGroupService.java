package com.hua.service;

import com.hua.entity.PageResult;
import com.hua.entity.QueryPageBean;
import com.hua.pojo.CheckGroup;

import java.util.List;

public interface CheckGroupService {
    /**
     * 获取检查组列表数据
     * @param queryPageBean
     * @return
     */
    PageResult findAll(QueryPageBean queryPageBean);

    /**
     * 添加检查组
     * @param checkitemIds
     * @param checkGroup
     */
    void add(Integer[] checkitemIds, CheckGroup checkGroup);

    /**
     * 删除检查组
     * @param id
     */
    void delById(Integer id);

    /**
     * 通过id获取检查组
     * @param id
     * @return
     */
    CheckGroup findById(Integer id);

    /**
     * 获取检查组对应的检查项
     * @return
     */
    List<Integer> findAssoicationCheckItem(Integer id);

    /**
     * 更新检查项
     * @param checkGroup
     * @param checkitemIds
     */
    void update(CheckGroup checkGroup, Integer[] checkitemIds);
}
