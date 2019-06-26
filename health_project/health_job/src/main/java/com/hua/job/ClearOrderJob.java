package com.hua.job;

import com.alibaba.dubbo.config.annotation.Reference;
import com.hua.service.OrderService;
import com.hua.service.OrderSettingService;
import com.hua.utils.DateUtils;

import java.util.Date;

/**
 * @author ：hua
 * @date ：Created in 2019/6/25
 * @description ：
 * @version: 1.0
 */
public class ClearOrderJob {

    @Reference
    OrderSettingService orderSettingService;

    /**
     * 定时清除预约数据
     * @throws Exception
     */
    public void orderClear() throws Exception {
        String date = DateUtils.parseDate2String(new Date());
        orderSettingService.orderClear(date);
        System.out.println("定时清除预约数据成功");
    }
}
