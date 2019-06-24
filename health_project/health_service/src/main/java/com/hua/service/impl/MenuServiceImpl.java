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
}
