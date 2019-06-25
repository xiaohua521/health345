package com.hua.utils;

import com.hua.constant.RedisConstant;
import org.springframework.beans.factory.annotation.Autowired;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

/**
 * 清空Redis缓冲
 * @author ：RuiQi
 * @date ：Created in 2019/6/24
 * @description ：
 * @version: 1.0
 */
public class JedisUtils {

    public static void clearRedis(JedisPool jedisPool,Integer id) {

        jedisPool.getResource().set(RedisConstant.SETMEAL+"-"+String.valueOf(id), "");
        jedisPool.getResource().set(RedisConstant.CHECKGROUPS+"-"+String.valueOf(id), "");
        jedisPool.getResource().set(RedisConstant.CHECKITEMS+"-"+String.valueOf(id), "");
        jedisPool.getResource().close();

    }

    public static void clearSetmealList(JedisPool jedisPool) {
        jedisPool.getResource().set(RedisConstant.SETMEAL_LIST, "");
        jedisPool.getResource().close();

    }
}
