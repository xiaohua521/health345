package com.hua.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.hua.dao.CheckGroupDao;
import com.hua.entity.PageResult;
import com.hua.entity.QueryPageBean;
import com.hua.pojo.CheckGroup;
import com.hua.service.CheckGroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import redis.clients.jedis.JedisPool;

import java.util.List;

/**
 * @author ：hua
 * @date ：Created in 2019/6/11
 * @description ：
 * @version: 1.0
 */
@Service(interfaceClass = CheckGroupService.class)
@Transactional
public class CheckGroupServiceImpl implements CheckGroupService {



    @Autowired
    CheckGroupDao checkGroupDao;

    /**
     * 获取检查组列表项数据
     * @param queryPageBean
     * @return
     */
    @Override
    @Transactional(readOnly = true,propagation = Propagation.SUPPORTS)
    public PageResult findAll(QueryPageBean queryPageBean) {
        PageHelper.startPage(queryPageBean.getCurrentPage(), queryPageBean.getPageSize());
        Page<CheckGroup> checkGroups =  checkGroupDao.findAll(queryPageBean.getQueryString());
        return new PageResult(checkGroups.getTotal(), checkGroups.getResult());
    }

    /**
     * 添加检查组
     * @param checkitemIds
     * @param checkGroup
     */
    @Override
    public void add(Integer[] checkitemIds, CheckGroup checkGroup) {
        checkGroupDao.addCheckGroup(checkGroup);

        //添加检查组关联的检查项
        addAssoication(checkitemIds,checkGroup.getId());
    }

    /**
     * 删除检查组
     * @param id
     */
    @Override
    public void delById(Integer id) {
        //判断检查组是否关联套餐管理
        int count  = checkGroupDao.isAssoicationMeal(id);
        if(count > 0){
            //存在关联的套餐
            throw new RuntimeException("该检查组已经关联套餐项,不能删除！！");
        }else{

            //删除检查组对应的检查项
            checkGroupDao.delAssoicationCheckItem(id);

            //删除检查组
            checkGroupDao.delById(id);
        }

    }

    /**
     * 通过id获取检查组
     * @param id
     * @return
     */
    @Override
    @Transactional(readOnly = true,propagation = Propagation.SUPPORTS)
    public CheckGroup findById(Integer id) {
        return checkGroupDao.findById( id);
    }

    /**
     * 获取检查组对应的检查项
     * @return
     */
    @Override
    @Transactional(readOnly = true,propagation = Propagation.SUPPORTS)
    public List<Integer> findAssoicationCheckItem(Integer id) {
        return checkGroupDao.findAssoicationCheckItem(id);
    }

    /**
     * 更新检查项
     * @param checkGroup
     * @param checkitemIds
     */
    @Override
    public void update(CheckGroup checkGroup, Integer[] checkitemIds) {
        //更新检查组
         checkGroupDao.update(checkGroup);
         //删除检查组对应的原检查项
         checkGroupDao.delAssoicationCheckItem(checkGroup.getId());
         //添加新更新的检查组对应的检查项
         this.addAssoication(checkitemIds, checkGroup.getId());
    }

    /**
     * 添加检查组关联的检查项
     * @param checkitemIds
     * @param checkGroupId
     */
    public void addAssoication(Integer[] checkitemIds,Integer checkGroupId){
        //如果检查组存在关联的检查项
        if(checkitemIds!=null && checkGroupId != null &&  checkitemIds.length > 0 ){
            for (Integer checkitemId : checkitemIds) {
                checkGroupDao.addCheckItem(checkitemId,checkGroupId);
            }
        }
    }
}
