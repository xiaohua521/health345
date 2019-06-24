package com.hua.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.hua.dao.CheckItemDao;
import com.hua.entity.PageResult;
import com.hua.pojo.CheckItem;
import com.hua.service.CheckItemService;
import com.hua.utils.JedisUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import redis.clients.jedis.JedisPool;

import java.util.List;

/**
 * @author ：hua
 * @date ：Created in 2019/6/9
 * @description ：
 * @version: 1.0
 */
@Service(interfaceClass = CheckItemService.class)
@Transactional
public class CheckItemServiceImpl implements CheckItemService {

    @Autowired
    CheckItemDao checkItemDao;

    @Autowired
    JedisPool jedisPool;

    /**
     * 分页查询
     * @param currentPage
     * @param pageSize
     * @param queryString
     * @return
     */
    @Override
    @Transactional(readOnly = true,propagation = Propagation.SUPPORTS)
    public PageResult findPage(Integer currentPage, Integer pageSize, String queryString) {
        PageHelper.startPage(currentPage,pageSize);
        Page<CheckItem> page = checkItemDao.findPage(queryString);
        return new PageResult(page.getTotal(),page.getResult());
    }

    /**
     * 添加检查项
     * @param checkItem
     */
    @Override
    public void add(CheckItem checkItem) {
        checkItemDao.add(checkItem);
        //清空缓冲
        clearRedis(checkItem.getId());
    }

    /**
     * 根据id删除检查项
     * @param id
     */
    @Override
    public void delById(Integer id) {
        //判定检查项是否关联检查组
        int count = checkItemDao.findByCheckItemId(id);
        if(count > 0){
            //存在关联的检查组，不能删除
            throw new RuntimeException("该检查项被检查组关联，不能删除！！");
        }else{
            //删除检查项
            checkItemDao.delById(id);
            //清空缓冲
            clearRedis(id);
        }


    }

    /**
     * 数据回显
     * @param id
     * @return
     */
    @Override
    @Transactional(readOnly = true,propagation = Propagation.SUPPORTS)
    public CheckItem findById(Integer id) {
        CheckItem  checkItem = checkItemDao.findById( id);
        return checkItem;
    }

    /**
     * 编辑检查项
     * @param checkItem
     */
    @Override
    public void update(CheckItem checkItem) {
        checkItemDao.update(checkItem);
        //清空缓冲
        clearRedis(checkItem.getId());
    }

    /**
     * 获取检查项
     * @return
     */
    @Override
    @Transactional(readOnly = true,propagation = Propagation.SUPPORTS)
    public List<CheckItem> findAll() {
        return checkItemDao.findAll();
    }

    /**
     *  清空缓冲
     *
     * @param id
     */
    public void clearRedis(Integer id) {
        //利用checkItem的id查询是否 与setmeal项关联,并且获取其关联的setmeal的id
        //如果有关联就清空缓冲
        List<Integer> setmealIds = checkItemDao.findById2Setmeal(id);

        if (setmealIds != null) {//不为空就清空缓冲
            for (Integer setmealId : setmealIds) {

                JedisUtils.clearRedis(jedisPool,setmealId);
            }
        }
    }
}
