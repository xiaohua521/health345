package com.hua.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.hua.dao.RoleDao;
import com.hua.pojo.Role;
import com.hua.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author ：wqzhang
 * @date ：Created in 2019/6/24
 * @description ：
 * @version: 1.0
 */
@Service(interfaceClass = RoleService.class)
@Transactional
public class RoleServiceImpl implements RoleService {
    @Autowired
    RoleDao roleDao;

    @Override
    public List<Role> findAll() {
        return roleDao.findAll();
    }
}
