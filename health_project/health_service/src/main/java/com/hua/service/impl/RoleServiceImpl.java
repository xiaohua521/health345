package com.hua.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.hua.dao.MenuDao;
import com.hua.dao.PermissionDao;
import com.hua.dao.RoleDao;
import com.hua.entity.PageResult;
import com.hua.entity.QueryPageBean;
import com.hua.pojo.Menu;
import com.hua.pojo.Permission;
import com.hua.pojo.Role;
import com.hua.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

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
    @Autowired
    PermissionDao permissionDao;

    @Autowired
    MenuDao menuDao;


    @Override
    public List<Role> findAll2() {
        return roleDao.findAll2();
    }





    /**
     * 数据回显
     */
    @Override
    @Transactional(readOnly = true,propagation = Propagation.SUPPORTS)
    public Map<String,Object> findById(Integer id) {
        HashMap<String, Object> map = new HashMap<>();
        Role role = roleDao.findById(id);

        List<Integer> menuIds = menuDao.findMenuId(id);
        List<Integer> permissionIds = permissionDao.findPermissionId(id);

        map.put("role", role);
        map.put("menuIds",menuIds);
        map.put("permissionIds", permissionIds);
        return map;
    }

    /**
     * 通过用户ID删除用户信息
     * @param id
     */
    @Override
    public void deleteById(Integer id) {
        //判断角色是否关联用户
        //int countRole =roleDao.isAssoicationRole(id);
        //System.out.println("countRole:"+countRole);
        //判断角色是否关联菜单
        int countMenu =roleDao.isAssoicationMenu(id);
        System.out.println("countMenu"+countMenu);
        //判断角色是否关联权限
        int countPermission =roleDao.isAssoicationPermission(id);
        System.out.println("countPermission"+countPermission);
        if( countMenu > 0 ||countPermission > 0){
            //存在关联的用户
            throw new RuntimeException("该管理角色已经关联用户,不能删除！！");
        }else{
            //删除用户角色
            roleDao.deleteByID(id);
        }
    }
    /**
     * 查询所有角色列表  进行分页
     * @param queryPageBean
     * @return
     */
    @Override
    public PageResult findAllRole(QueryPageBean queryPageBean) {
        PageHelper.startPage(queryPageBean.getCurrentPage(), queryPageBean.getPageSize());
        Page<Role> roles=roleDao.findAll(queryPageBean.getQueryString());
        return new PageResult(roles.getTotal(), roles.getResult());
    }

    /**
     *  修改的方法
     *
     * @param role
     * @param menuIds
     * @param permissionIds
     */
    @Override
    public void edit(Role role, Integer[] menuIds, Integer[] permissionIds) {
        //修改role对象
        roleDao.edit(role);
        //删除角色和菜单的关联
        menuDao.remove(role.getId());
        //删除角色和权限的关联
        permissionDao.remove(role.getId());
        //添加角色和菜单的关联
        addRole2Menu(role.getId(),menuIds);
        //添加角色和权限的关联
        addRole2Permission(role.getId(),permissionIds);
    }

    /**
     *  新增的方法
     * @param role
     * @param menuIds
     * @param permissionIds
     */
    @Override
    public void add(Role role, Integer[] menuIds, Integer[] permissionIds) {
        //添加role对象
        roleDao.add(role);
        //添加role和menu关联
        addRole2Menu(role.getId(),menuIds);
        //添加role和permission的关联
        addRole2Permission(role.getId(),permissionIds);
    }


    /**
     *  添加角色和菜单的关联
     * @param roleId
     * @param menuIds
     */
    public void addRole2Menu(Integer roleId,Integer[] menuIds){
        for (Integer menuId : menuIds) {
            menuDao.addRole2Menu(roleId, menuId);
        }
    }

    /**
     *  添加角色和权限的关联
     * @param roleId
     * @param permissionIds
     */
    public void addRole2Permission(Integer roleId,Integer[] permissionIds){
        for (Integer permissionId : permissionIds) {
            permissionDao.addRole2Permission(roleId, permissionId);
        }
    }

    /**
     *  页面信息展示
     * @return
     */
    @Override
    public Role findAll() {
        Role role = new Role();
        Set<Permission> permissions = permissionDao.findAll();
        LinkedHashSet<Menu> menus = menuDao.findAll();
        role.setPermissions(permissions);
        role.setMenus(menus);
        return role;
    }
}
