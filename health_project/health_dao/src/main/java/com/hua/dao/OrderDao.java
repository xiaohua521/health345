package com.hua.dao;

import com.hua.pojo.Order;

import java.util.Map;

public interface OrderDao {
    /*判断当前会员是否有预约该日的套餐*/
    long findByCondition(Order order);

    //添加预约信息
    void addOrder(Order order);

    //获取预约成功信息
    Map<String, Object> findById(Integer id);

    /**
     * 今日预约数
     * @param reportDate
     * @return
     */
    long getTodayOrderCount(String reportDate);

    /**
     * 今日到诊数
     * @param reportDate
     * @return
     */
    long getTodayVisitCount(String reportDate);

    /**
     * 本周预约数
     * @param thisfirstDayOfWeek
     * @return
     */
    long getThisWeekOrderCount(String thisfirstDayOfWeek);

    /**
     * 本周到诊数
     * @param thisfirstDayOfWeek
     * @return
     */
    long getThisWeekVisitCount(String thisfirstDayOfWeek);

    /**
     * 本月预约数
     * @param firstDay4ThisMonth
     * @return
     */
    long getThisMonthOrderCount(String firstDay4ThisMonth);

    /**
     * 本月到诊数
     * @param firstDay4ThisMonth
     * @return
     */
    long getThisMonthVisitCount(String firstDay4ThisMonth);

}
