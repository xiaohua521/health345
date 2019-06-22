package com.hua.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.hua.constant.MessageConstant;
import com.hua.entity.Result;
import com.hua.pojo.OrderSetting;
import com.hua.service.OrderSettingService;
import com.hua.utils.POIUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author ：hua
 * @date ：Created in 2019/6/13
 * @description ：
 * @version: 1.0
 */
@RestController
@RequestMapping("/ordersetting")
public class OrderSettingController {

    @Reference
    OrderSettingService orderSettingService;

    @RequestMapping("/upload")
    public Result upload(@RequestBody MultipartFile excelFile){
        try {
            //解析excel文件,返回cell集合
            List<String[]> cells = POIUtils.readExcel(excelFile);
            //创建OrderSetting集合
            List<OrderSetting> orderSettingList = new ArrayList<>();
            //遍历list集合封装OrderSetting到集合
            for (String[] cell : cells) {
                OrderSetting orderSetting = new OrderSetting();
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
                orderSetting.setOrderDate(sdf.parse(cell[0]));
                orderSetting.setNumber(Integer.parseInt(cell[1]));
                orderSettingList.add(orderSetting);
            }
            //上传数据
            orderSettingService.upload(orderSettingList);
            return new Result(true, MessageConstant.IMPORT_ORDERSETTING_SUCCESS);

        }catch(RuntimeException e){
            return new Result(false, e.getMessage());

        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.IMPORT_ORDERSETTING_FAIL);
        }
    }

    /**
     * 获取当月预约信息
     * @param month
     * @return
     */
    @RequestMapping("/findByMonth")
    public Result findByMonth(String month){
        try {
            //获取当月预约信息
            List<OrderSetting> orderSettingList = orderSettingService.findByMonth(month);
            //拼接返回值
            List<Object> list = new ArrayList<>();
            for (OrderSetting orderSetting : orderSettingList) {
                Map<String,Object> map = new HashMap<>();
                int date = orderSetting.getOrderDate().getDate();
                int number = orderSetting.getNumber();
                int reservations = orderSetting.getReservations();
                map.put("date", date);
                map.put("number", number);
                map.put("reservations", reservations);
                list.add(map);
            }
            return new Result(true, MessageConstant.QUERY_ORDER_SUCCESS,list);

        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.QUERY_ORDER_FAIL);
        }
    }

    /**
     * 设置当天可预约人数
     * @return
     */
    @RequestMapping("/edit")
    public Result edit(@RequestBody OrderSetting orderSetting){
        try {
            List<OrderSetting> orderSettingList = new ArrayList<>();
            orderSettingList.add(orderSetting);
            orderSettingService.edit(orderSettingList);
            return new Result(true, MessageConstant.ORDERSETTING_SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.ORDERSETTING_FAIL);
        }
    }
}
