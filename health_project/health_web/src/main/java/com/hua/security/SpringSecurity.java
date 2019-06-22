package com.hua.security;

import com.alibaba.dubbo.config.annotation.Reference;
import com.hua.pojo.Permission;
import com.hua.pojo.Role;
import com.hua.pojo.SysUser;
import com.hua.service.UserService;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.ArrayList;
import java.util.List;

/**
 * @author ：hua
 * @date ：Created in 2019/6/19
 * @description ：
 * @version: 1.0
 */
public class SpringSecurity implements UserDetailsService {

    @Reference
    UserService userService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        //查询数据库获取用户信息
        SysUser sysUser = userService.findByUserName(username);

        if(sysUser == null){
            //如果不存在该用户信息
            return null;
        }

        //定义权限集合
        List<GrantedAuthority> grantedAuthorityList = new ArrayList<>();

        //获取用户角色
        for (Role role : sysUser.getRoles()) {
            //获取用户权限
            for (Permission permission : role.getPermissions()) {
                //实例化权限对象
                SimpleGrantedAuthority simpleGrantedAuthority = new SimpleGrantedAuthority(permission.getKeyword());
                grantedAuthorityList.add(simpleGrantedAuthority);
            }
        }
        //创建安全框架User对象
        UserDetails userDetails = new User(sysUser.getUsername(), sysUser.getPassword(),grantedAuthorityList);

        return userDetails;
    }
}
