package com.hua.service;

import com.hua.entity.PageResult;
import com.hua.entity.QueryPageBean;
import com.hua.pojo.Menu;

import java.util.List;
import java.util.Map;

public interface MenuService {
    /**
     * 获取菜单
     * @param queryPageBean
     * @return
     */
    PageResult findPage(QueryPageBean queryPageBean);


    /**
     * 添加菜单
     * @param menu
     */
    void add(Menu menu);

    /**
     * 回显数据
     * @param id
     * @return
     */
    Menu findById(Integer id);

    /**
     * 更新菜单
     * @param menu
     */
    void update(Menu menu);

    /**
     * 删除菜单
     * @param id
     */
    void delById(Integer id);

    /**
     * 动态展示菜单栏
     * @param username
     * @return
     */

    List<Map<String,Object>> getUserMenu(String username);
}
