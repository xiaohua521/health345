package com.hua.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.hua.dao.MemberDao;
import com.hua.pojo.Member;
import com.hua.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * @author ：hua
 * @date ：Created in 2019/6/18
 * @description ：
 * @version: 1.0
 */
@Service(interfaceClass = MemberService.class)
@Transactional
public class MemberServiceImpl implements MemberService {

    @Autowired
    MemberDao memberDao;

    /**
     * 判断是否是会员
     * @param telephone
     * @return
     */
    @Override
    @Transactional(readOnly = true,propagation = Propagation.SUPPORTS)
    public Member findByTelephone(String telephone) {
        return memberDao.findMemberByPhoneNum(telephone);
    }

    /**
     * 注册会员
     * @param member
     */
    @Override
    public void reg(Member member) {
        memberDao.addMember(member);
    }

    /**
     * 统计每月会员数量
     * @param months
     * @return
     */
    @Override
    public List<Integer> getReportMemberCount(List<String> months) {
        List<Integer> memberCount = new ArrayList<>();
        for (String month : months) {
            Integer count = memberDao.findMemberCount(month);
            memberCount.add(count);
        }
        return memberCount;
    }
}
