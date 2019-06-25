package com.hua.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.hua.dao.PermissionDao;
import com.hua.entity.PageResult;
import com.hua.pojo.Permission;
import com.hua.service.PermissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author ：MMMaybe
 * @date ：Created in 2019/6/24
 * @description ：
 * @version: 1.0
 */
@Service(interfaceClass = PermissionService.class)
@Transactional
public class PermissionServiceImpl implements PermissionService {


    @Autowired
    PermissionDao permissionDao;
    /**
     * 分页查询权限列表
     * @param
     * @return
     */
    @Override
    public PageResult findPage(Integer currentPage, Integer pageSize, String queryString) {
        PageHelper.startPage(currentPage,pageSize);
       Page<Permission> page =  permissionDao.findPage(queryString);
       return new PageResult(page.getTotal(),page.getResult());

    }
    /**
     * 根据ID查询权限
     * @param id
     * @return
     */
    @Override
    public Permission findById(Integer id) {
        return permissionDao.findById(id);
    }
    /**
     * 添加权限
     * @param permission
     * @return
     */
    @Override
    public void add(Permission permission) {
        permissionDao.add(permission);
    }
    /**
     * 根据ID删除权限
     * @param id
     * @return
     */
    @Override
    public void deleteById(Integer id) {
        permissionDao.deleteById(id);
    }
    /**
     * 编辑更新权限信息
     * @param permission
     * @return
     */
    @Override
    public void update(Permission permission) {
        permissionDao.update(permission);
    }
}
