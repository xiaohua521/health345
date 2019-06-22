package com.hua.service;

import com.hua.pojo.SysUser;

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
}
