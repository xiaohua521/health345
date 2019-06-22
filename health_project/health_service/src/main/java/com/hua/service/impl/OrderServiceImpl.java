package com.hua.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.hua.constant.MessageConstant;
import com.hua.dao.MemberDao;
import com.hua.dao.OrderDao;
import com.hua.dao.OrderSettingDao;
import com.hua.entity.Result;
import com.hua.pojo.Member;
import com.hua.pojo.Order;
import com.hua.pojo.OrderSetting;
import com.hua.service.OrderService;
import com.hua.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.Map;

/**
 * @author ：hua
 * @date ：Created in 2019/6/16
 * @description ：
 * @version: 1.0
 */
@Service(interfaceClass = OrderService.class )
@Transactional
public class OrderServiceImpl implements OrderService {

    @Autowired
    OrderSettingDao orderSettingDao;

    @Autowired
    MemberDao memberDao;

    @Autowired
    OrderDao orderDao;



    /**
     * 添加预约
     * @param map
     * @return
     */
    @Override
    public Result addOrder(Map<String, Object> map) {
        //获取预约的日期
        Object orderDateObj = map.get("orderDate");
        String orderDateStr = String.valueOf(orderDateObj);

        //获取电话号
        String telephone = String.valueOf(map.get("telephone"));

        //日期字符串转Date类型
        Date orderDate = null;
        try {
            orderDate  = DateUtils.parseString2Date(orderDateStr);
        } catch (Exception e) {
            e.printStackTrace();
        }

        //获取套餐id
        Integer setmealId = Integer.parseInt(String.valueOf(map.get("setmealId")));


        //判断当前日期是否有预约设置
        OrderSetting orderSetting = orderSettingDao.findByOrderDate(orderDate);
        if(orderSetting == null){
            //未预约设置
            return new Result(false, MessageConstant.SELECTED_DATE_CANNOT_ORDER);
        }

        //判断当前日期预约设置人数是否已满
        if(orderSetting.getReservations() >= orderSetting.getNumber()){
            //当天预约人数已满
            return new Result(false, MessageConstant.ORDER_FULL);
        }

        //判断当前用户是否是会员
        Member member = memberDao.findMemberByPhoneNum(telephone);
        //获取会员的id
//        Integer memberId = member.getId();

        if(member == null){
            //不是会员注册为会员
            member = new Member();
            //会员姓名
            member.setName(String.valueOf(map.get("name")));
            //会员性别
            member.setSex(String.valueOf(map.get("sex")));
            //会员身份证号
            member.setIdCard(String.valueOf(map.get("idCard")));
            //手机号
            member.setPhoneNumber(telephone);
            //注册时间
            member.setRegTime(new Date());

            //添加会员信息(主键回显)
            memberDao.addMember(member);

        }else{
            //是会员
            //进一步判断当前会员是否有预约该日的套餐

            Order order = new Order();
            order.setOrderDate(orderDate);
            order.setMemberId( member.getId());
            order.setSetmealId(setmealId);
            long count = orderDao.findByCondition(order);
            if(count > 0){
                //已经预约该日的套餐
                return new Result(false, MessageConstant.HAS_ORDERED);
            }
        }

        //添加预约信息(主键回显)
        Order order = new Order();
        order.setOrderDate(orderDate);
        order.setMemberId( member.getId());
        order.setSetmealId(setmealId);
        order.setOrderType(String.valueOf(map.get("orderType")));
        order.setOrderStatus(Order.ORDERSTATUS_NO);
        orderDao.addOrder(order);

        //更新预约设置人数
        orderSetting.setReservations(orderSetting.getReservations() + 1);
        orderSettingDao.editByOrderDate(orderSetting);

        return new Result(true, MessageConstant.ORDER_SUCCESS,order);
    }


    //获取预约成功信息
    @Override
    public Map<String, Object> findById(Integer id) {
        return orderDao.findById(id);
    }
}
