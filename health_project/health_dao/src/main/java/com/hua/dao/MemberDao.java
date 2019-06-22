package com.hua.dao;

import com.hua.pojo.Member;

public interface MemberDao {

    //判断当前用户是否是会员
    Member findMemberByPhoneNum(String telephone);

    //注册会员
    void addMember(Member member);

    /**
     * 统计每月会员数量
     * @param month
     * @return
     */
    Integer findMemberCount(String month);

    /**
     * 新增会员数
     * @param reportDate
     * @return
     */
    long getTodayAddMemberCount(String reportDate);

    /**
     * 会员总数
     * @param
     * @return
     */
    long getMemberTotalCount();

    /**
     * 本周新增会员数
     * @param thisfirstDayOfWeek
     * @return
     */
    long getThisWeekAddMemberCount(String thisfirstDayOfWeek);

    /**
     * 本月新增会员数
     * @param firstDay4ThisMonth
     * @return
     */
    long getThisMonthAddMemberCount(String firstDay4ThisMonth);
}
