package com.hua.controller;

import com.aliyuncs.exceptions.ClientException;
import com.hua.constant.MessageConstant;
import com.hua.constant.RedisConstant;
import com.hua.constant.RedisMessageConstant;
import com.hua.entity.Result;
import com.hua.utils.SMSUtils;
import com.hua.utils.ValidateCodeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import redis.clients.jedis.JedisPool;

/**
 * @author ：hua
 * @date ：Created in 2019/6/16
 * @description ：发送验证码 并存储到redis
 * @version: 1.0
 */
@RestController
@RequestMapping("/validateCode")
public class ValidateCodeController {

    @Autowired
    JedisPool jedisPool;

    @RequestMapping("/send4Order")
    public Result send4Order(String telephone){

        //生成验证码
        Integer validateCode = ValidateCodeUtils.generateValidateCode(4);
        //发送验证码到手机
        try {
            SMSUtils.sendShortMessage(SMSUtils.VALIDATE_CODE, telephone,String.valueOf(validateCode));
        } catch (ClientException e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.SEND_VALIDATECODE_FAIL);
        }
        //发送成功存储验证码到redis
        //定义redis的key为： 001+"电话号码"
        String key = RedisMessageConstant.SENDTYPE_ORDER  + "-" +telephone;
        //定时存储5分钟
        jedisPool.getResource().setex(key,300,String.valueOf(validateCode));

        return new Result(false, MessageConstant.SEND_VALIDATECODE_SUCCESS);
    }


    /**
     * 发送登录验证码
     * @param telephone
     * @return
     */
    @RequestMapping("/send4Login")
    public Result send4Login(String telephone){
            //生成验证码
            Integer validateCode = ValidateCodeUtils.generateValidateCode(4);
            //发送验证码
        try {
            SMSUtils.sendShortMessage(SMSUtils.VALIDATE_CODE,telephone,String.valueOf(validateCode));
        } catch (ClientException e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.SEND_VALIDATECODE_FAIL);
        }
        //存储redis
        jedisPool.getResource().setex(RedisMessageConstant.SENDTYPE_LOGIN + "-" + telephone, 300, String.valueOf(validateCode));
        return new Result(true, MessageConstant.SEND_VALIDATECODE_SUCCESS);
    }
}
