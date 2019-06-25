package com.hua.service;

import com.hua.entity.PageResult;
import com.hua.entity.QueryPageBean;
import com.hua.pojo.Role;

import java.util.List;
import java.util.Map;

/**
 * @author ：wqzhang
 * @date ：Created in 2019/6/24
 * @description ：
 * @version: 1.0
 */
public interface RoleService {
    /**
     * 查询所有角色
     * @return
     */
    List<Role> findAll2();

    /**
     *  查询权限,菜单信息展示
     * @return
     */
    Role findAll();

    /**
     *  添加的方法
     * @param role
     * @param menuIds
     * @param permissionIds
     */
    void add(Role role, Integer[] menuIds, Integer[] permissionIds);

    /**
     *  修改的方法
     * @param role
     * @param menuIds
     * @param permissionIds
     */
    void edit(Role role, Integer[] menuIds, Integer[] permissionIds);

    /**
     * 分页查询
     * @param queryPageBean
     * @return
     */
    PageResult findAllRole(QueryPageBean queryPageBean);

    /**
     *  删除方法
     * @param id
     */
    void deleteById(Integer id);
    /**
     * 数据回显
     * @param id
     * @return
     */
    Map<String,Object> findById(Integer id);
}
