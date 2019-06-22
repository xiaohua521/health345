package com.hua.dao;

import com.hua.pojo.Role;

import java.util.Set;

public interface RoleDao {
    /**
     * 根据用户id获取角色
     * @param id
     * @return
     */
    Set<Role> findRoleListByUserId(Integer id);
}
