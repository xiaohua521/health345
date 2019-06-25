package com.hua.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.hua.constant.MessageConstant;
import com.hua.entity.PageResult;
import com.hua.entity.QueryPageBean;
import com.hua.entity.Result;
import com.hua.pojo.Role;
import com.hua.service.RoleService;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * @author ：wqzhang
 * @date ：Created in 2019/6/24
 * @description ：
 * @version: 1.0
 */
@RestController
@RequestMapping("/role")
public class RoleController {

    @Reference
    RoleService roleService;
    /**
     * 查询所用角色
     * @return
     */
    @RequestMapping("/findAll2")
    public Result findAll2() {
        try {
            List<Role> roleList = roleService.findAll2();
            return new Result(true, MessageConstant.QUERY_ROLE_SUCCESS,roleList);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.QUERY_ROLE_FAIL);
        }
    }


    /**
     * 数据回显
     * @param id
     * @return
     */
    @RequestMapping("/findById")
    public Result findById(Integer id){
        try {
            Map<String,Object> map = roleService.findById(id);
            return new Result(true, "", map);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, "");
        }
    }

    /**
     * 通过用户ID删除用户信息
     * @param id
     * @return
     */
    @RequestMapping("/delById")
    public Result delById(Integer id) {
        try {
            //通过ID查询用户信息
            roleService.deleteById(id);
            return new Result(true, MessageConstant.DELETE_ROLE_LIST_SUCCESS);
        } catch(RuntimeException e){
            e.printStackTrace();
            return new Result(false, e.getMessage());
        }catch(Exception e){
            e.printStackTrace();
            return new Result(false, MessageConstant.DELETE_ROLE_LIST_FAIL);
        }
    }


    /**
     * 查询所有角色列表
     * @param queryPageBean
     * @return
     */
    @RequestMapping("/findAllRole")
    public PageResult findAllRole(@RequestBody QueryPageBean queryPageBean){
        try {
            return roleService.findAllRole(queryPageBean);
            //return new Result(true, MessageConstant.QUERY_ROLE_LIST_SUCCESS);
        }catch (Exception e){
            e.printStackTrace();
            //return new Result(false, MessageConstant.QUERY_ROLE_LIST_FAIL);
            return null;
        }
    }

    /**
     *  修改的方法
     * @param role
     * @param menuIds
     * @param permissionIds
     * @return
     */
    @RequestMapping("/update")
    public Result update(@RequestBody Role role, Integer[] menuIds, Integer[] permissionIds){
        try {
            roleService.edit(role, menuIds, permissionIds);
            return new Result(true, MessageConstant.EDIT_ROLE_LIST_SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.EDIT_ROLE_LIST_FAIL);
        }
    }


    /**
     *  查询权限,菜单信息展示
     * @return
     */
    @RequestMapping("/findAll")
    public Result findAll(){
        try {
            Role role = roleService.findAll();
            return new Result(true, MessageConstant.QUERY_ROLE_LIST_SUCCESS, role);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.QUERY_ROLE_LIST_FAIL);
        }
    }


    /**
     *  添加的方法
     * @param role
     * @param menuIds
     * @param permissionIds
     * @return
     */
    @RequestMapping("/add")
    public Result add(@RequestBody Role role, Integer[] menuIds, Integer[] permissionIds) {
        try {
            roleService.add(role,menuIds,permissionIds);
            return new Result(true, MessageConstant.ADD_ROLE_LIST_SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.ADD_ROLE_LIST_FAIL);
        }
    }
}
