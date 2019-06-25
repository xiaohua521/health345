package com.hua.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.hua.constant.MessageConstant;
import com.hua.entity.PageResult;
import com.hua.entity.QueryPageBean;
import com.hua.entity.Result;
import com.hua.pojo.Permission;
import com.hua.service.PermissionService;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author ：MMMaybe
 * @date ：Created in 2019/6/24
 * @description ：
 * @version: 1.0
 */
@RestController
@RequestMapping("/permission")
public class PermissionController {


    @Reference
    PermissionService permissionService;

    /**
     * 分页查询权限列表
     * @param queryPageBean
     * @return
     */
    @RequestMapping("/findPage")
    public PageResult findPage(@RequestBody QueryPageBean queryPageBean){
        try {
            PageResult pageResult = permissionService.findPage(queryPageBean.getCurrentPage(), queryPageBean.getPageSize(), queryPageBean.getQueryString());
            return pageResult;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 根据ID查询权限
     * @param id
     * @return
     */
    @RequestMapping("/findById")
    public Result findById(Integer id){
        try {
            Permission permission = permissionService.findById(id);
            return new Result(true, MessageConstant.QUERY_PERMISSION_SUCCESS,permission);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,MessageConstant.QUERY_PERMISSION_FAIL);
        }
    }

    /**
     * 添加权限
     * @param permission
     * @return
     */
    @RequestMapping("/add")
    public Result add(@RequestBody Permission permission){
        try {
            permissionService.add(permission);
            return new Result(true,MessageConstant.ADD_PERMISSION_SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,MessageConstant.ADD_PERMISSION_FAIL);
        }
    }

    /**
     * 根据ID删除权限
     * @param id
     * @return
     */
    @RequestMapping("/delById")
    public Result deleteById(Integer id){
        try {
            permissionService.deleteById(id);
            return new Result(true,MessageConstant.DELETE_PERMISSION_SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,MessageConstant.DELETE_PERMISSION_FAIL);
        }
    }

    /**
     * 编辑更新权限信息
     * @param permission
     * @return
     */
    @RequestMapping("/update")
    public Result update(@RequestBody Permission permission){
        try {
            permissionService.update(permission);
            return new Result(true,MessageConstant.EDIT_PERMISSION_SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,MessageConstant.EDIT_PERMISSION_FAIL);
        }
    }
}
