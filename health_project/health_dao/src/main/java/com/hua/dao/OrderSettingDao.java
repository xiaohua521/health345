package com.hua.dao;

import com.hua.pojo.OrderSetting;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

/**
 * @author ：hua
 * @date ：Created in 2019/6/13
 * @description ：
 * @version: 1.0
 */
public interface OrderSettingDao {
    /**
     * 批量添加
     * @param orderSetting
     */
    void upload(OrderSetting orderSetting);

    /**
     * 判断是否存在冲突日期
     * @param orderDate
     * @return
     */
    OrderSetting findByOrderDate(Date orderDate);

    /*
     * 更新冲突日期可预约数量
     * @param orderSetting
     */
    void editByOrderDate(OrderSetting orderSetting);

    /**
     * 获取当月预约信息
     * @param beginDate
     * @param endDate
     * @return
     */
    List<OrderSetting> findByMonth(@Param("beginDate") String beginDate,@Param("endDate")  String endDate);

    /**
     * 设置当天可预约人数
     * @param orderSetting
     */
    void edit(OrderSetting orderSetting);
}
