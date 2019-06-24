package com.hua.dao;

import com.github.pagehelper.Page;
import com.hua.pojo.Menu;

public interface MenuDao {
    /**
     * 获取菜单
     * @param queryString
     * @return
     */
    Page<Menu> findPage(String queryString);

    /**
     * 添加菜单
     * @param menu
     */
    void add(Menu menu);

    /**
     *回显数据
     * @param id
     * @return
     */
    Menu findById(Integer id);

    /**
     * 更新数据
     * @param menu
     */
    void update(Menu menu);

    /**
     * 删除菜单
     * @param id
     */
    void delById(Integer id);
}
