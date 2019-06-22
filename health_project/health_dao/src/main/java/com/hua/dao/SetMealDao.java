package com.hua.dao;

import com.github.pagehelper.Page;
import com.hua.pojo.CheckGroup;
import com.hua.pojo.Setmeal;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface SetMealDao {
    /**
     * 获取检查组
      * @return
     */
    List<CheckGroup> findAllCheckGroup();

    /**
     * 添加套餐
     * @param setmeal
     */
    void add(Setmeal setmeal);


    /**
     * 添加套餐关联的检查组
     * @param setMealId
     * @param checkgroupId
     */
    void addCheckGroup(@Param("setMealId") Integer setMealId,@Param("checkgroupId") Integer checkgroupId);

    /**
     * 分页获取套餐
     * @param queryString
     * @return
     */
    Page<Setmeal> findPage(String queryString);

    /**
     * 获取所有套餐
     * @return
     */
    List<Setmeal> getSetmeal();

    /**
     * 获取套餐详情(套餐+检查组+检查项)
     * @param id
     * @return
     */
    Setmeal findById(Integer id);

    /**
     * 统计套餐数量
     * @return
     */
    List<Map<String, String>> findSetMealCount();

    /**
     * 获取热门套餐
     * @return
     */
    List<Map<String, Object>> getHotSetMeal();
}
