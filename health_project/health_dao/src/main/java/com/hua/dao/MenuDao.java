package com.hua.dao;

import com.github.pagehelper.Page;
import com.hua.pojo.Menu;

import java.util.List;

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


    //根据用户名查询用户id
    int getUserId(String username);
    //根据用户ID查询到角色id
    int getRoleId(int userId);
    //根据角色id查询到对应菜单id
    List<Integer> getMenuId(int roleId);
    //根据菜单id查询到菜单信息
    Menu getMenu(Integer menuId);

    List<Menu> getMenuById(Integer id);
}
