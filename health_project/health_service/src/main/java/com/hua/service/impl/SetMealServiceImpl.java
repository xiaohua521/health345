package com.hua.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.hua.constant.RedisConstant;
import com.hua.dao.CheckGroupDao;
import com.hua.dao.CheckItemDao;
import com.hua.dao.SetMealDao;
import com.hua.entity.PageResult;
import com.hua.entity.QueryPageBean;
import com.hua.pojo.CheckGroup;
import com.hua.pojo.CheckItem;
import com.hua.pojo.Setmeal;
import com.hua.service.SetMealService;
import com.hua.utils.JedisUtils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import redis.clients.jedis.JedisPool;

import java.util.HashMap;
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

    @Autowired
    CheckGroupDao checkGroupDao;

    @Autowired
    CheckItemDao checkItemDao;



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

        //清空redis缓冲
        JedisUtils.clearSetmealList(jedisPool);
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
        //从redis中获取套餐列表
        //redis中存储套餐列表信息的名称为 RedisConstant.SETMEAL_LIST
        //从jedis中获取数据,判断是否存在,如果不存在就从dao层调用方法获取数据存入到redis中
        String JedisSetmealList = jedisPool.getResource().get(RedisConstant.SETMEAL_LIST);

        //创建返回值list集合为null,方便下边直接调用
        List<Setmeal> list = null;
        //判断
        if (JedisSetmealList == null || "".equals(JedisSetmealList)) {//为空,直接掉数据库查询
            System.out.println("从数据库查询套餐列表数据      1");
            //从数据库查询
            list = setMealDao.getSetmeal();
            //将数据转换为JSON字符串
            String jsonArray = JSONArray.toJSONString(list);
            //存入到redis中
            jedisPool.getResource().set(RedisConstant.SETMEAL_LIST, jsonArray);
            //释放资源
            jedisPool.getResource().close();
        }else{//不为空,redis中有数据
            System.out.println("从redis中获取套餐列表数据      1");
            //将从redis中获取到的JSON字符串数据转换为list集合形式
            list = JSONArray.parseArray(JedisSetmealList, Setmeal.class);
        }
        return list;
    }

    /**
     * 获取套餐详情(套餐+检查组+检查项)
     * @param id
     * @return
     */
    @Override
    public Setmeal findById(Integer id) {
        System.out.println("你好啊"+id);
        //从redis中获取套餐详情信息
        //redis中存储套餐列表信息的名称为 RedisConstant.SETMEAL_PARTICULARS
        //从jedis中获取数据,判断是否存在,如果不存在就从dao层调用方法获取数据存入到redis中
        String jsonSetmeal = jedisPool.getResource().get(RedisConstant.SETMEAL+"-"+String.valueOf(id));
        String jsonCheckGroup = jedisPool.getResource().get(RedisConstant.CHECKGROUPS+"-"+String.valueOf(id));
        String jsonCheckItem = jedisPool.getResource().get(RedisConstant.CHECKITEMS+"-"+String.valueOf(id));
        //创建返回值setmeal对象为null,方便下边直接调用
        Setmeal setmeal = null;

        //判断
        if (jsonSetmeal == null || "".equals(jsonSetmeal) ||
                jsonCheckGroup == null || "".equals(jsonCheckGroup) ||
                jsonCheckItem == null || "".equals(jsonCheckItem)) {//为空,从数据库查询
            System.out.println("从数据查询套餐详情信息       2");
            //setmeal = setMealDao.findById(id);
            //查询setmeal对象
            setmeal = setMealDao.findByIdSetmeal(id);
            //将上边查到的数据存入到redis中
            String JedisSetmeal = JSONObject.toJSONString(setmeal);
            jedisPool.getResource().set(RedisConstant.SETMEAL+"-"+String.valueOf(id), JedisSetmeal);

            //通过setmeal的id查询封装在setmeal中的checkGroup对象集合
            List<CheckGroup> checkGroups = checkGroupDao.findBySetmealId(id);
            //存入redis中
            //将其转换为JSON字符串
            String jedisCheckGroups = JSONArray.toJSONString(checkGroups);
            jedisPool.getResource().set(RedisConstant.CHECKGROUPS+"-"+String.valueOf(id), jedisCheckGroups);

            Map<String, List<CheckItem>> map = new HashMap<>();

            for (CheckGroup checkGroup : checkGroups) {
                //通过checkgroup的id查询封装在其内的checkItem集合
                List<CheckItem> checkItems = checkItemDao.findByCheckGroupId(checkGroup.getId());
                checkGroup.setCheckItems(checkItems);
                //添加到map集合中
                map.put(checkGroup.getName(), checkItems);

            }
            //存入redis中
            //将其转换为JSON字符串
            String jsonMap = JSONObject.toJSONString(map);
            jedisPool.getResource().set(RedisConstant.CHECKITEMS + "-" + String.valueOf(id), jsonMap);

            //将信息封装到setmeal对象中
            setmeal.setCheckGroups(checkGroups);


        }else{//不为空从redis中拿取
            System.out.println("从redis中获取套餐详情数据      2");
            //获取setmeal对象
            JSONObject obj = JSON.parseObject(jsonSetmeal);
            String name = obj.getString("name");
            String code = obj.getString("code");
            String helpCode = obj.getString("helpCode");
            String sex = obj.getString("sex");
            String age = obj.getString("age");
            String price = obj.getString("price");
            String remark = obj.getString("remark");
            String attention = obj.getString("attention");
            String img = obj.getString("img");
            setmeal = new Setmeal();
            setmeal.setId(id);
            setmeal.setName(name);
            setmeal.setCode(code);
            setmeal.setHelpCode(helpCode);
            setmeal.setSex(sex);
            setmeal.setAge(age);
            setmeal.setPrice(Float.parseFloat(price));
            setmeal.setRemark(remark);
            setmeal.setAttention(attention);
            setmeal.setImg(img);
            //获取CheckGroups集合
            List<CheckGroup> checkGroups = JSONArray.parseArray(jsonCheckGroup, CheckGroup.class);
            JSONObject jsonObject = JSONObject.parseObject(jsonCheckItem);
            for (CheckGroup checkGroup : checkGroups) {
                List<CheckItem> list = (List<CheckItem>) jsonObject.get(checkGroup.getName());
                checkGroup.setCheckItems(list);
            }
            setmeal.setCheckGroups(checkGroups);
        }
        System.out.println(setmeal);
        return setmeal;
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
