package com.hua.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.hua.constant.MessageConstant;
import com.hua.constant.RedisConstant;
import com.hua.entity.PageResult;
import com.hua.entity.QueryPageBean;
import com.hua.entity.Result;
import com.hua.pojo.CheckGroup;
import com.hua.pojo.Setmeal;
import com.hua.service.SetMealService;
import com.hua.utils.QiniuUtils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

/**
 * @author ：hua
 * @date ：Created in 2019/6/12
 * @description ：
 * @version: 1.0
 */
@RestController
@RequestMapping("/setmeal")
public class SetMealController {

    @Reference
    SetMealService setMealService;

    @Autowired
    JedisPool jedisPool;

    /**
     * 上传图片到七牛云服务器
     * @return
     */
    @RequestMapping("/upload")
    public Result upload(@RequestBody MultipartFile imgFile){
        try {
            System.out.println(imgFile);
            //获取文件名
            String filename = imgFile.getOriginalFilename();

            //获取文件名的扩展名
            String extName = filename.substring(filename.lastIndexOf("."));
            //获取随机UUID
            String uuid = UUID.randomUUID().toString().replace("-", "");
            //拼接文件名，保证文件名唯一
            String uuidName = uuid + extName;
            //上传图片到图片服务器
            QiniuUtils.upload2Qiniu(imgFile.getBytes(),uuidName);

            //把上传成功后的图片名称存在redis中
            jedisPool.getResource().sadd(RedisConstant.SETMEAL_PIC_RESOURCES, uuidName);

            return new Result(true, MessageConstant.PIC_UPLOAD_SUCCESS,uuidName);
        } catch (IOException e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.PIC_UPLOAD_FAIL);
        }
    }

    /**
     * 添加套餐
     * @return
     */
    @RequestMapping("/add")
    public Result add(Integer[] checkgroupIds, @RequestBody Setmeal setmeal){
        try {
            setMealService.add(checkgroupIds,setmeal);
            return new Result(true, MessageConstant.QUERY_SETMEAL_SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.QUERY_SETMEAL_FAIL);
        }

    }

    /**
     * 获取检查组列表
     * @return
     */
    @RequestMapping("/findAllCheckGroup")
    public Result findAllCheckGroup(){
        try {
            List<CheckGroup> checkGroupList = setMealService.findAllCheckGroup();
            return new Result(true, MessageConstant.QUERY_CHECKGROUP_SUCCESS, checkGroupList);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(true, MessageConstant.QUERY_CHECKGROUP_FAIL);
        }
    }

    /**
     * 分页获取套餐
     * @return
     */
    @RequestMapping("/findPage")
    public PageResult findPage(@RequestBody QueryPageBean queryPageBean){
        return setMealService.findPage(queryPageBean);
    }
}
