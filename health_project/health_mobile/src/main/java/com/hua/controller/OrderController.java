package com.hua.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.hua.constant.MessageConstant;
import com.hua.constant.RedisMessageConstant;
import com.hua.entity.Result;
import com.hua.service.OrderService;
import com.hua.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import redis.clients.jedis.JedisPool;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

/**
 * @author ：hua
 * @date ：Created in 2019/6/16
 * @description ：
 * @version: 1.0
 */
@RestController
@RequestMapping("/order")
public class OrderController {

    @Autowired
    JedisPool jedisPool;

    @Reference
    OrderService orderService;

    /**
     * 提交预约订单
     * @return
     */
    @RequestMapping("/submit")
    public Result submit(@RequestBody Map<String,Object> map){
        //校验用户输入的验证码和redis存储的是否一致
        //获取输入的验证码
        Object validateCodeObj = map.get("validateCode");
        String validateCodeStr = String.valueOf(validateCodeObj);
        //获取电话号码
        Object telephoneObj = map.get("telephone");
        String telephoneStr = String.valueOf(telephoneObj);
        //获取redis存储的验证码
        String redisValidateCode = jedisPool.getResource().get(RedisMessageConstant.SENDTYPE_ORDER + "-" + telephoneStr);
        //判断用户输入的验证码与redis中存储的是否一致
        if(redisValidateCode == null || !redisValidateCode.equals(validateCodeStr)){

            //redis为空（发送验证码失败）  或者 校验不一致
            return  new Result(false, MessageConstant.VALIDATECODE_ERROR);
        }else{
            //验证成功
            //添加预约方式
            map.put("orderType", "微信预约");
            Result result = orderService.addOrder(map);
            return result;
        }
    }


    //获取预约成功信息
    @RequestMapping("/findById")
    public Result findById(Integer id){
        try {
            Map<String,Object> map = orderService.findById(id);
            //日期格式化
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            String date = sdf.format(map.get("orderDate"));
            map.put("orderDate",date);
            return new Result(true, MessageConstant.QUERY_ORDER_SUCCESS, map);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.QUERY_ORDER_FAIL);
        }
    }
}
