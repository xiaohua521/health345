package com.hua.dao;

import com.github.pagehelper.Page;
import com.hua.pojo.Role;

import java.util.List;
import java.util.Set;

public interface RoleDao {


    /**
     * 查询所有角色
     * @return
     */
    List<Role> findAll2();


    /**
     * 根据用户id获取角色
     * @param id
     * @return
     */
    Set<Role> findRoleListByUserId2(Integer id);

    Set<Role> findRoleListByUserId(Integer id);


    /**
     *  添加用户
     * @param role
     */
    void add(Role role);

    /**
     *  通过id修改用户
     * @param role
     */
    void edit(Role role);

    /**
     *  分页查询
     * @param queryString
     * @return
     */
    Page<Role> findAll(String queryString);

    int isAssoicationRole(Integer id);

    int isAssoicationMenu(Integer id);

    int isAssoicationPermission(Integer id);

    void deleteByID(Integer id);

    Role findById(Integer id);
}
