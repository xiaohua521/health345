package com.hua.dao;

import com.hua.pojo.Permission;

import java.util.Set;

public interface PermissionDao {

    /**
     * 根据角色id获取权限
     * @param id
     * @return
     */
    Set<Permission> findPermissionListFindByRoleId(Integer id);
}
