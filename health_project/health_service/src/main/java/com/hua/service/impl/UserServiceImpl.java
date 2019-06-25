package com.hua.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.hua.dao.UserDao;
import com.hua.entity.PageResult;
import com.hua.entity.QueryPageBean;
import com.hua.pojo.SysUser;
import com.hua.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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

    /**
     * 分页查询
     * @param queryPageBean
     * @return
     */
    @Override
    @Transactional(readOnly = true,propagation = Propagation.SUPPORTS)
    public PageResult findPage(QueryPageBean queryPageBean) {
        PageHelper.startPage(queryPageBean.getCurrentPage(),queryPageBean.getPageSize());
        Page<SysUser> page = userDao.findByPage(queryPageBean.getQueryString());
        return new PageResult(page.getTotal(),page.getResult());
    }

    /**
     * 修改用户信息
     * @param user  用户
     * @param roleIds  用户关联的角色ids
     */
    @Override
    public void update(SysUser user, Integer[] roleIds) {
        //修改用户
        userDao.update(user);
        //删除关系(用户和角色的关系)
        userDao.delAssociation(user.getId());
        //维护新关系
        setSysUserAndRole(user.getId(),roleIds);
    }

    /**
     * 根据用户id查询用户数据
     * @param id
     * @return
     */
    @Override
    @Transactional(readOnly = true,propagation = Propagation.SUPPORTS)
    public SysUser findById(Integer id) {
        return userDao.findById(id);
    }

    /**
     * 根据用户id查询角色ids
     * @param id
     * @return
     */
    @Override
    @Transactional(readOnly = true,propagation = Propagation.SUPPORTS)
    public List<Integer> findRoleIdsById(Integer id) {
        return userDao.findRoleIdsById(id);
    }

    @Override
    public void add(Integer[] roleIds, SysUser user) {
        //添加用户(主键回显用于中间表的添加数据)
        userDao.add(user);
        setSysUserAndRole(user.getId(),roleIds);
    }

    @Override
    public void delById(Integer id) {
        //删除用户和角色的关系
        userDao.delAssociation(id);
        userDao.delById(id);
    }

    /**
     * 维护用户和角色的关系
     * @param id        用户id
     * @param roleIds   角色ids
     */
    private void setSysUserAndRole(Integer id, Integer[] roleIds) {
        if (id != null && roleIds != null && roleIds.length > 0) {
            for (Integer roleId : roleIds) {
                userDao.insert(id,roleId);
            }
        }
    }
}
