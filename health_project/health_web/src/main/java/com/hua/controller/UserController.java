package com.hua.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.hua.constant.MessageConstant;
import com.hua.entity.PageResult;
import com.hua.entity.QueryPageBean;
import com.hua.entity.Result;
import com.hua.pojo.SysUser;
import com.hua.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/**
 * @author ：hua
 * @date ：Created in 2019/6/19
 * @description ：
 * @version: 1.0
 */
@RestController
@RequestMapping("/user")
public class UserController {
    @Reference
    UserService userService;
    @Autowired
    BCryptPasswordEncoder bCryptPasswordEncoder;
    @RequestMapping("/getUserName")
    public Result getUserName(HttpServletRequest request){
        //方法一（不建议）：getAttributeNames  获取session域中所有的属性名,遍历枚举类型， SPRING_SECURITY_CONTEXT
        //Enumeration attributeNames =  request.getSession().getAttributeNames();


        //方法二
        //获取安全框架上下文对象
        SecurityContext securityContext = SecurityContextHolder.getContext();
        //获取认证对象
        Authentication authentication = securityContext.getAuthentication();
        //principal: 重要信息（User）
        User user = (User)authentication.getPrincipal();
        //获取用户名
        String username = user.getUsername();

        return new Result(true, MessageConstant.GET_USERNAME_SUCCESS, username);
    }

    @RequestMapping("/findByPage")
    @PreAuthorize("hasAuthority('USER_QUERY')")
    public PageResult findPage(@RequestBody QueryPageBean queryPageBean) {
        try {
             return userService.findPage(queryPageBean);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    @RequestMapping("/findById")
    @PreAuthorize("hasAuthority('USER_QUERY')")
    public Result findById(Integer id) {
        try {
            SysUser user = userService.findById(id);
            return new Result(true, MessageConstant.QUERY_USER_SUCCESS,user);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.QUERY_USER_FAIL);
        }
    }
    @RequestMapping("/findRoleIdsById")
    @PreAuthorize("hasAuthority('ROLE_QUERY')")
    public Result findRoleIdsById(Integer id) {
        try {
            List<Integer> roleIds = userService.findRoleIdsById(id);
            return new Result(true, MessageConstant.QUERY_ROLE_SUCCESS,roleIds);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.QUERY_ROLE_FAIL);
        }
    }

    @RequestMapping("/update")
    @PreAuthorize("hasAuthority('USER_EDIT')")
    public Result update(@RequestBody Map<String,Object> map) {
        try {
            //反序列化
            //获取用户关联的角色id
            JSONArray jsonArray = (JSONArray) map.get("roleIds");
            Integer[] roleIds = jsonArray.toArray(new Integer[]{});
            //获取用户信息
            JSONObject jsonObject = (JSONObject) map.get("user");
            SysUser user = jsonObject.toJavaObject(SysUser.class);
            user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
            //更新用户user
            userService.update(user,roleIds);
            return new Result(true, MessageConstant.EDIT_USER_SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.EDIT_USER_FAIL);
        }
    }

    @RequestMapping("/add")
    @PreAuthorize("hasAuthority('USER_ADD')")
    public Result add(Integer[] roleIds, @RequestBody SysUser user) {
        try {
            user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
            userService.add(roleIds,user);
            return new Result(true, MessageConstant.ADD_USER_SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.ADD_USER_FAIL);
        }
    }

    @RequestMapping("/delById")
    @PreAuthorize("hasAuthority('USER_DELETE')")
    public Result delById(Integer id) {
       try {
           userService.delById(id);
           return new Result(true, MessageConstant.DELETE_USER_SUCCESS);
       } catch (Exception e) {
           e.printStackTrace();
           return new Result(false, MessageConstant.DELETE_USER_FAIL);
       }
    }
}
