package com.hua.dao;

import com.github.pagehelper.Page;
import com.hua.entity.PageResult;
import com.hua.pojo.CheckItem;

import java.util.List;

public interface CheckItemDao {
    /**
     * 分页查询
     * @param queryString
     * @return
     */
    Page<CheckItem> findPage(String queryString);

    /**
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
     * 根据检查项id查找是否关联检查组
     * @param id
     * @return
     */
    int findByCheckItemId(Integer id);

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

    /**
     *  通过checkGroup的id进行查询
     * @param id
     * @return
     */
    List<CheckItem> findByCheckGroupId(Integer id);

    /**
     *  通过checkItem的id查询setmeal的id
     * @param id
     * @return
     */
    List<Integer> findById2Setmeal(Integer id);
}
