package com.hua.service;

import com.hua.entity.PageResult;
import com.hua.entity.QueryPageBean;
import com.hua.pojo.CheckGroup;
import com.hua.pojo.Setmeal;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
import java.util.Map;

/**
 * @author ：hua
 * @date ：Created in 2019/6/12
 * @description ：
 * @version: 1.0
 */
public interface SetMealService {
    /**
     * 获取检查组
     * @return
     */
    List<CheckGroup> findAllCheckGroup();

    /**
     * 添加套餐
     * @param setmeal
     */
    void add(Integer[] checkgroupIds,Setmeal setmeal);

    /**
     * 分页获取套餐
     * @param queryPageBean
     * @return
     */
    PageResult findPage(QueryPageBean queryPageBean);

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
}
