package com.hua.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.hua.dao.MenuDao;
import com.hua.entity.PageResult;
import com.hua.entity.QueryPageBean;
import com.hua.pojo.Menu;
import com.hua.service.MenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author ：hua
 * @date ：Created in 2019/6/24
 * @description ：
 * @version: 1.0
 */
@Service(interfaceClass =MenuService.class )
@Transactional
public class MenuServiceImpl implements MenuService {

    @Autowired
    MenuDao menuDao;

    /**
     * 获取菜单
     * @param queryPageBean
     * @return
     */
    @Override
    @Transactional(readOnly = true,propagation = Propagation.SUPPORTS)
    public PageResult findPage(QueryPageBean queryPageBean) {
        PageHelper.startPage(queryPageBean.getCurrentPage(), queryPageBean.getPageSize());
        Page<Menu> menus = menuDao.findPage(queryPageBean.getQueryString());
        return new PageResult(menus.getTotal(), menus.getResult());
    }

    /**
     * 添加菜单
     * @param menu
     */
    @Override
    public void add(Menu menu) {
        menuDao.add(menu);
    }

    /**
     * 回显数据
     * @param id
     * @return
     */
    @Override
    @Transactional(readOnly = true,propagation = Propagation.SUPPORTS)
    public Menu findById(Integer id) {
        return menuDao.findById(id);
    }

    /**
     * 更新菜单
     * @param menu
     */
    @Override
    public void update(Menu menu) {
        menuDao.update(menu);
    }

    /**
     * 删除菜单
     * @param id
     */
    @Override
    public void delById(Integer id) {
        menuDao.delById(id);
    }

    /**
     * 动态展示菜单栏
     * @param username
     * @return
     */
    @Override
    public List<Map<String,Object>> getUserMenu(String username) {
        //根据用户名查询到用户角色
        //根据用户名查询用户id
        int userId = menuDao.getUserId(username);
        //根据用户ID查询到角色id
        int roleId = menuDao.getRoleId(userId);
        //根据角色id查询到对应菜单id
        List<Integer> menuIds = menuDao.getMenuId(roleId);
        //创建一个集合存放菜单信息
        List<Map<String,Object>> menuList = new ArrayList<>();

        for (Integer menuId : menuIds) {
            //创建一个map存放一级菜单  放在for循环中避免覆盖
            Map<String,Object> map1 = new HashMap<>();
            //根据菜单id查询到菜单信息
            Menu menu = menuDao.getMenu(menuId);
            //说明该菜单是一级菜单
            if (menu.getParentMenuId() == null){
                //把需要返回的数据装到map中
                map1.put("path",menu.getPath());
                map1.put("title",menu.getName());
                map1.put("icon",menu.getIcon());
                //map1.put("children",menu.getChildren());
                //查找出与menu_id相等的ParentMenuId
                List<Menu> list1 = menuDao.getMenuById(menu.getId());
                List<Map<String,Object>> menuList2 = new ArrayList<>();

                for (Menu menu1 : list1) {
                    //创建一个map存放子菜单的信息 放在for循环中避免被覆盖
                    Map<String,Object> map2 = new HashMap<>();
                    map2.put("path",menu1.getPath());
                    map2.put("title",menu1.getName());
                    map2.put("linkUrl",menu1.getLinkUrl());
                    map2.put("children",menu1.getChildren());
                    menuList2.add(map2);
                }
                map1.put("children",menuList2);
                menuList.add(map1);
            }
        }
        return menuList;
    }
}
