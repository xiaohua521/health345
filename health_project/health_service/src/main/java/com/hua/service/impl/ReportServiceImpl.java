package com.hua.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.hua.dao.MemberDao;
import com.hua.dao.OrderDao;
import com.hua.dao.SetMealDao;
import com.hua.service.ReportService;
import com.hua.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * @author ：hua
 * @date ：Created in 2019/6/21
 * @description ：
 * @version: 1.0
 */

@Service(interfaceClass = ReportService.class)
@Transactional
public class ReportServiceImpl implements ReportService {

    @Autowired
    MemberDao memberDao;

    @Autowired
    OrderDao orderDao;

    @Autowired
    SetMealDao setMealDao;
    /**
     * 运营数据统计
     * @return
     */
    @Override
    @Transactional(readOnly = true,propagation = Propagation.SUPPORTS)
    public Map<String, Object> getBusinessReportData() throws Exception {
        //封装统计数据集合
        Map<String,Object> map = new HashMap<>();

        //获取统计日期---当前日期
        String reportDate = DateUtils.parseDate2String(new Date());

        //会员数据统计

        //新增会员数
        long todayAddMemberCount = memberDao.getTodayAddMemberCount(reportDate);
        //会员总数
        long memberTotalCount = memberDao.getMemberTotalCount();

        //本周新增会员数
        //获取本周周一日期
        String thisfirstDayOfWeek =  DateUtils.parseDate2String(DateUtils.getThisWeekMonday());
        long thisWeekAddMemberCount = memberDao.getThisWeekAddMemberCount(thisfirstDayOfWeek);

        //本月新增会员数
        //获取本月1号的日期
        String firstDay4ThisMonth =DateUtils.parseDate2String(DateUtils.getFirstDay4ThisMonth());
        long thisMonthAddMemberCount  = memberDao.getThisMonthAddMemberCount(firstDay4ThisMonth);

        //预约到诊数据统计
        //今日预约数
        long todayOrderCount = orderDao.getTodayOrderCount(reportDate);
        //今日到诊数
        long todayVisitCount = orderDao.getTodayVisitCount(reportDate);
        //本周预约数
        long thisWeekOrderCount = orderDao.getThisWeekOrderCount(thisfirstDayOfWeek);
        //本周到诊数
        long thisWeekVisitCount = orderDao.getThisWeekVisitCount(thisfirstDayOfWeek);
        //本月预约数
        long thisMonthOrderCount = orderDao.getThisMonthOrderCount(firstDay4ThisMonth);
        //本月到诊数
        long thisMonthVisitCount = orderDao.getThisMonthVisitCount(firstDay4ThisMonth);

        //热门套餐--查询预约最多的套餐 （前三名）
        List<Map<String,Object>> HotSetMealList = setMealDao.getHotSetMeal();

        map.put("reportDate",reportDate);
        map.put("todayNewMember",todayAddMemberCount);
        map.put("totalMember",memberTotalCount);
        map.put("thisWeekNewMember",thisWeekAddMemberCount);
        map.put("thisMonthNewMember",thisMonthAddMemberCount);
        map.put("todayOrderNumber",todayOrderCount);
        map.put("todayVisitsNumber",todayVisitCount);
        map.put("thisWeekOrderNumber",thisWeekOrderCount);
        map.put("thisWeekVisitsNumber",thisWeekVisitCount);
        map.put("thisMonthOrderNumber",thisMonthOrderCount);
        map.put("thisMonthVisitsNumber",thisMonthVisitCount);
        map.put("hotSetmeal",HotSetMealList);

        return map;
    }
}
