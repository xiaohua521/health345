package com.hua.service;

import com.hua.pojo.Member;

import java.util.List;

public interface MemberService {
    /**
     * 判断是否是会员
     * @param telephone
     * @return
     */
    Member findByTelephone(String telephone);


    /**
     * 注册会员
     * @param member
     */
    void reg(Member member);

    List<Integer> getReportMemberCount(List<String> months);

    int findStartDateMember(String format1);

}
