package com.hua.dao;

import com.github.pagehelper.Page;
import com.hua.pojo.SysUser;

import java.util.List;

public interface UserDao {

    /**
     * 根据用户名获取用户信息
     * @param username
     * @return
     */
    SysUser findByUserName(String username);


    /**
     * 分页查询(条件查询)
     * @param queryString
     * @return
     */
    Page<SysUser> findByPage(String queryString);

    /**
     * 修改用户信息
     * @param user
     */
    void update(SysUser user);

    /**
     * 删除用户和角色的关系
     * @param id
     */
    void delAssociation(Integer id);

    /**
     * 添加用户和角色的关系
     * @param id
     * @param roleId
     */
    void insert(Integer id, Integer roleId);

    /**
     * 根据用户id查询用户数据
     * @param id
     * @return
     */
    SysUser findById(Integer id);

    /**
     * 根据用户id查询角色ids
     * @param id
     * @return
     */
    List<Integer> findRoleIdsById(Integer id);

    /**
     * 添加用户
     * @param user
     */
    void add(SysUser user);

    /**
     * 根据id删除用户
     * @param id
     */
    void delById(Integer id);
}
