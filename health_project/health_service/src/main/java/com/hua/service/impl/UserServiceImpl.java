package com.hua.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.hua.dao.UserDao;
import com.hua.pojo.SysUser;
import com.hua.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author ：hua
 * @date ：Created in 2019/6/19
 * @description ：
 * @version: 1.0
 */
@Service(interfaceClass = UserService.class)
@Transactional
public class UserServiceImpl implements UserService {

    @Autowired
    UserDao userDao;

    /**
     * 根据用户名获取用户信息
     * @param username
     * @return
     */
    @Override
    @Transactional(readOnly = true,propagation = Propagation.SUPPORTS)
    public SysUser findByUserName(String username) {
        return userDao.findByUserName(username);
    }
}
