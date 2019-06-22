package com.hua.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.hua.constant.MessageConstant;
import com.hua.constant.RedisMessageConstant;
import com.hua.entity.Result;
import com.hua.pojo.Member;
import com.hua.service.MemberService;
import javassist.expr.NewExpr;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.Map;

/**
 * @author ：hua
 * @date ：Created in 2019/6/18
 * @description ：
 * @version: 1.0
 */
@RestController
@RequestMapping("/login")
public class LoginController {

    @Autowired
    JedisPool jedisPool;

    @Reference
    MemberService memberService;


    @RequestMapping("/check")
    public Result check (@RequestBody Map<String,Object> map, HttpServletResponse response){
        //获取验证码
        String validateCode = String.valueOf(map.get("validateCode"));
        //获取电话号
        String telephone = String.valueOf(map.get("telephone"));
        //与发送的验证码进行比对
        String redisValidateCode = jedisPool.getResource().get(RedisMessageConstant.SENDTYPE_LOGIN + "-" + telephone);
        if(redisValidateCode==null || !redisValidateCode.equals(validateCode)){
            //验证码不一致
            return new Result(false, MessageConstant.VALIDATECODE_ERROR);
        }
        //验证码一致
        //判断用户是否注册会员
        Member member = memberService.findByTelephone(telephone);
        if(member ==null){
            //注册会员
            member = new Member();
            member.setPhoneNumber(telephone);
            member.setRegTime(new Date());
            memberService.reg(member);
        }
        //存入cookie
        Cookie cookie = new Cookie("telephone", telephone);
        //设置cookie的获取范围
        cookie.setPath("/");
        //生存时间
        cookie.setMaxAge(60 * 60 * 24 * 31);
        response.addCookie(cookie);
        return new Result(true, MessageConstant.LOGIN_SUCCESS);
    }
}
