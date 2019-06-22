package com.hua.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.hua.constant.RedisConstant;
import com.hua.dao.SetMealDao;
import com.hua.entity.PageResult;
import com.hua.entity.QueryPageBean;
import com.hua.pojo.CheckGroup;
import com.hua.pojo.Setmeal;
import com.hua.service.SetMealService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import redis.clients.jedis.JedisPool;

import java.util.List;
import java.util.Map;

/**
 * @author ：hua
 * @date ：Created in 2019/6/12
 * @description ：
 * @version: 1.0
 */
@Service(interfaceClass = SetMealService.class)
@Transactional
public class SetMealServiceImpl implements SetMealService {

    @Autowired
    JedisPool jedisPool;

    @Autowired
    SetMealDao setMealDao;

    /**
     * 获取检查组
     *
     * @return
     */
    @Override
    @Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
    public List<CheckGroup> findAllCheckGroup() {
        return setMealDao.findAllCheckGroup();
    }

    /**
     * 添加套餐
     *
     * @param setmeal
     */
    @Override
    public void add(Integer[] checkgroupIds, Setmeal setmeal) {
        //添加套餐
        setMealDao.add(setmeal);
        //添加套餐关联的检查组
        addSetMealAndCheckGroup(checkgroupIds, setmeal.getId());
        //将上传的图片存入redis
        jedisPool.getResource().sadd(RedisConstant.SETMEAL_PIC_DB_RESOURCES, setmeal.getImg());
    }

    /**
     * 分页获取套餐
     * @param queryPageBean
     * @return
     */
    @Override
    @Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
    public PageResult findPage(QueryPageBean queryPageBean) {
        PageHelper.startPage(queryPageBean.getCurrentPage(), queryPageBean.getPageSize());
        Page<Setmeal> page = setMealDao.findPage(queryPageBean.getQueryString());
        return new PageResult(page.getTotal(), page.getResult());
    }

    /**
     * 获取所有套餐
     * @return
     */
    @Override
    public List<Setmeal> getSetmeal() {
        return setMealDao.getSetmeal();
    }

    /**
     * 获取套餐详情(套餐+检查组+检查项)
     * @param id
     * @return
     */
    @Override
    public Setmeal findById(Integer id) {
        return setMealDao.findById(id);
    }

    /**
     * 统计套餐数量
     * @return
     */
    @Override
    public List<Map<String, String>> findSetMealCount() {
        return setMealDao.findSetMealCount();
    }

    /**
     * 添加套餐关联的检查组
     *
     * @param checkgroupIds
     * @param setMealId
     */
    public void addSetMealAndCheckGroup(Integer[] checkgroupIds, Integer setMealId) {
        if (checkgroupIds != null && checkgroupIds.length > 0 && setMealId != null) {
            for (Integer checkgroupId : checkgroupIds) {
                setMealDao.addCheckGroup(setMealId,checkgroupId);
            }
        }
    }
}
