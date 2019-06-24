package com.hua.service;

import com.hua.entity.PageResult;
import com.hua.pojo.Permission;

public interface PermissionService {
    /**
     * 分页查询权限列表
     * @param
     * @return
     */
    PageResult findPage(Integer currentPage, Integer pageSize, String queryString);
    /**
     * 根据ID查询权限
     * @param id
     * @return
     */
    Permission findById(Integer id);
    /**
     * 添加权限
     * @param permission
     * @return
     */
    void add(Permission permission);
    /**
     * 根据ID删除权限
     * @param id
     * @return
     */
    void deleteById(Integer id);
    /**
     * 编辑更新权限信息
     * @param permission
     * @return
     */
    void update(Permission permission);
}
