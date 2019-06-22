package com.hua.job;

import com.hua.constant.RedisConstant;
import com.hua.utils.QiniuUtils;
import redis.clients.jedis.JedisPool;

import java.util.Set;

/**
 * @author ：hua
 * @date ：Created in 2019/6/12
 * @description ：清除图片服务器无用图片
 * @version: 1.0
 */
public class ClearImgJob {

    JedisPool jedisPool;

    public void setJedisPool(JedisPool jedisPool) {
        this.jedisPool = jedisPool;
    }


    public void clear(){
        Set<String> sdiff = jedisPool.getResource().sdiff(RedisConstant.SETMEAL_PIC_RESOURCES, RedisConstant.SETMEAL_PIC_DB_RESOURCES);
        for (String imgName : sdiff) {
            //清除图片服务器
            QiniuUtils.deleteFileFromQiniu(imgName);
            //清除redis
            jedisPool.getResource().srem(RedisConstant.SETMEAL_PIC_RESOURCES,imgName);
            System.out.println(imgName);
        }
    }
}
