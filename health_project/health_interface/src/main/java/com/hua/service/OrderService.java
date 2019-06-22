package com.hua.service;

import com.hua.entity.Result;

import java.util.Map;

/**
 * @author ：hua
 * @date ：Created in 2019/6/16
 * @description ：
 * @version: 1.0
 */
public interface OrderService {
    /**
     * 添加预约
     * @param map
     * @return
     */
    Result addOrder(Map<String, Object> map);

    /**
     * 获取预约成功信息
     * @param id
     * @return
     */
    Map<String, Object> findById(Integer id);
}
