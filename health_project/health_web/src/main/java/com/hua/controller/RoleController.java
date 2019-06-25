package com.hua.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.hua.constant.MessageConstant;
import com.hua.entity.Result;
import com.hua.pojo.Role;
import com.hua.service.RoleService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

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
    @RequestMapping("/findAll")
    public Result findAll() {
        try {
            List<Role> roleList = roleService.findAll();
            return new Result(true, MessageConstant.QUERY_ROLE_SUCCESS,roleList);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.QUERY_ROLE_FAIL);
        }
    }
}
