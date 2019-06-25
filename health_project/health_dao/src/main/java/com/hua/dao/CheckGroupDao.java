package com.hua.dao;

import com.github.pagehelper.Page;
import com.hua.pojo.CheckGroup;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface CheckGroupDao {
    /**
     * 获取检查组列表项数据
     * @param queryString
     * @return
     */
    Page<CheckGroup> findAll(String queryString);

    /**
     * 添加检查组
     * @param checkGroup
     */
    void addCheckGroup(CheckGroup checkGroup);

    /**
     * 添加检查组关联的检查项
     * @param checkitemId
     * @param checkGroupId
     */
    void addCheckItem(@Param("checkitemId")Integer checkitemId,@Param("checkGroupId")Integer checkGroupId);

    /**
     * 删除检查组
     * @param id
     */
    void    delById(Integer id);

    /**
     * 判定检查组是否关联套餐管理
     * @param id
     * @return
     */
    int isAssoicationMeal(Integer id);

    /**
     * 删除关联的检查项
     * @param id
     */
    void delAssoicationCheckItem(Integer id);


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
     * 更新检查组
     * @param checkGroup
     */
    void update(CheckGroup checkGroup);

    /**
     *  通过setmeal的id查询
     * @param id
     * @return
     */
    List<CheckGroup> findBySetmealId(Integer id);

    /**
     *  通过checkGroup的id查询setmeal的id
     *
     * @param id
     * @return
     */
    List<Integer> findById2setmeal(Integer id);
}
