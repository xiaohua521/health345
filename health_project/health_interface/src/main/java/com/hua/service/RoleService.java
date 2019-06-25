package com.hua.service;

import com.hua.pojo.Role;

import java.util.List;

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
    List<Role> findAll();
}
