package com.hua.service;

import com.hua.pojo.OrderSetting;

import java.util.List;

public interface OrderSettingService {
    /**
     *批量添加每日可预约数量
     * @param orderSettingList
     */
    void upload(List<OrderSetting> orderSettingList);

    /**
     * 获取当月预约信息
     * @param month
     * @return
     */
    List<OrderSetting> findByMonth(String month);

    /**
     * 设置当天可预约人数
     * @param orderSetting
     */
    void edit(List<OrderSetting> orderSetting);

    /**
     * 定时清除预约数据
     * @param date
     */
    void orderClear(String date);
}
