package com.hua.security;

import com.hua.pojo.Role;
import com.hua.pojo.SysUser;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.ArrayList;
import java.util.List;

/**
 * @author ：hua
 * @date ：Created in 2019/6/18
 * @description ：
 * @version: 1.0
 */
public class SpringSecurity implements UserDetailsService {

    public static void main(String[] args) {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String encode = passwordEncoder.encode("123");
        System.out.println(encode);
    }

    //模拟数据库的数据
    private static List<SysUser>  userList = new ArrayList<>();

    static{
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        SysUser user1 = new SysUser();
        user1.setUsername("zhangsan");
//        user1.setPassword("{noop}1234");
        user1.setPassword(bCryptPasswordEncoder.encode("1234"));

        Role role1 = new Role();
        role1.setName("ROLE_ADMIN");
        user1.getRoles().add(role1);
        Role role3 = new Role();
        role3.setName("add");
        user1.getRoles().add(role3);

        SysUser user2 = new SysUser();
        user2.setUsername("lisi");
     /*   user2.setPassword("{noop}1234");*/
        user2.setPassword(bCryptPasswordEncoder.encode("1234"));
        Role role2 = new Role();
        role2.setName("ROLE_USER");
        user2.getRoles().add(role2);

        Role role4 = new Role();
        role4.setName("delete");
        user2.getRoles().add(role4);

        userList.add(user1);
        userList.add(user2);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        //根据用户名从数据库查询用户信息
        for (SysUser sysUser : userList) {
            if(username!=null && username.equals(sysUser.getUsername())){
                //创建角色的集合对象
                ArrayList<GrantedAuthority> list = new ArrayList<>();
                for (Role role : sysUser.getRoles()) {
                    SimpleGrantedAuthority simpleGrantedAuthority = new SimpleGrantedAuthority(role.getName());
                    list.add(simpleGrantedAuthority);
                }
                //2. 返回User（安全框架定义）对象即可
                User user = new User(sysUser.getUsername(),sysUser.getPassword(),list);
                return user;
            }
        }
        return null;
    }
}
