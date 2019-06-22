package com.hua.controller;

import com.hua.constant.MessageConstant;
import com.hua.entity.Result;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Enumeration;

/**
 * @author ：hua
 * @date ：Created in 2019/6/19
 * @description ：
 * @version: 1.0
 */
@RestController
@RequestMapping("/user")
public class UserController {

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
}
