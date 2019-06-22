package com.hua.dao;

import com.hua.pojo.SysUser;

public interface UserDao {

    /**
     * 根据用户名获取用户信息
     * @param username
     * @return
     */
    SysUser findByUserName(String username);
}
