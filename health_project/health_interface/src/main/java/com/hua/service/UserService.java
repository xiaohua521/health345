package com.hua.service;

import com.hua.entity.PageResult;
import com.hua.entity.QueryPageBean;
import com.hua.pojo.SysUser;

import java.util.List;

/**
 * @author ：hua
 * @date ：Created in 2019/6/19
 * @description ：
 * @version: 1.0
 */
public interface UserService {
    /**
     * 根据姓名获取用户信息
     * @param username
     * @return
     */
    SysUser findByUserName(String username);

    /**
     * 分页查询
     * @param queryPageBean
     * @return
     */
    PageResult findPage(QueryPageBean queryPageBean);

    /**
     * 更新用户
     * @param user  用户
     * @param roleIds  用户关联的角色ids
     */
    void update(SysUser user, Integer[] roleIds);

    /**
     * 根据 id查询用户数据
     * @param id
     * @return
     */
    SysUser findById(Integer id);

    /**
     * 根据用户id查询关联的角色ids
     * @param id
     * @return
     */
    List<Integer> findRoleIdsById(Integer id);

    /**
     * 添加用户
     * @param roleIds 角色ids
     * @param user
     */
    void add(Integer[] roleIds, SysUser user);

    /**
     * 删除用户
     * @param id
     */
    void delById(Integer id);
}
