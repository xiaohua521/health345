package com.hua.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.hua.dao.OrderSettingDao;
import com.hua.pojo.OrderSetting;
import com.hua.service.OrderSettingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import javax.swing.text.EditorKit;
import java.util.List;

/**
 * @author ：hua
 * @date ：Created in 2019/6/13
 * @description ：
 * @version: 1.0
 */
@Service(interfaceClass = OrderSettingService.class)
@Transactional
public class OrderSettingServiceImpl implements OrderSettingService {

    @Autowired
    OrderSettingDao orderSettingDao;

    /**
     * 批量添加每日可预约数量
     *
     * @param orderSettingList
     */
    @Override
    public void upload(List<OrderSetting> orderSettingList) {
        //判断是否存在冲突日期
        for (OrderSetting orderSetting : orderSettingList) {
            OrderSetting existOrderSetting = isExistOrderDate(orderSetting);
            if (existOrderSetting != null) {
                //存在冲突日
                //进一步判断已经预约人数 是否超过 添加的每日可预约数量
                if (orderSetting.getNumber() > existOrderSetting.getReservations()) {
                    //未超过，进行编辑修改
                    editByOrderDate(orderSetting);
                } else {
                    //超过，抛出异常
                    throw new RuntimeException("存在冲突日，已经预约人数超过每日可预约数量，不能上传!!!");

                }
            } else {
                //不存在冲突，直接添加
                orderSettingDao.upload(orderSetting);
            }
        }
    }

    /**
     * 获取当月预约信息
     * @param month
     * @return
     */
    @Override
    public List<OrderSetting> findByMonth(String month) {
        String beginDate  = month + "-" + "01";
        String endDate  = month + "-" + "31";
        return orderSettingDao.findByMonth(beginDate,endDate);
    }


    /**
     * 设置当天可预约人数
     * @param orderSetting
     */
    @Override
    public void edit(List<OrderSetting> orderSetting) {
        for (OrderSetting setting : orderSetting) {
            editByOrderDate(setting);
        }
    }


    /**
     * 判断是否存在冲突日期
     *
     * @param orderSetting
     * @return
     */
    public OrderSetting isExistOrderDate(OrderSetting orderSetting) {
        OrderSetting existOrderDate = orderSettingDao.findByOrderDate(orderSetting.getOrderDate());
        return existOrderDate;
    }

    /**
     * 更新冲突日可预约数量
     *
     * @param orderSetting
     */
    public void editByOrderDate(OrderSetting orderSetting) {
        orderSettingDao.editByOrderDate(orderSetting);
    }



}
