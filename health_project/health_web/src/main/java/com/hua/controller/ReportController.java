package com.hua.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.hua.constant.MessageConstant;
import com.hua.entity.Result;
import com.hua.pojo.Setmeal;
import com.hua.service.MemberService;
import com.hua.service.ReportService;
import com.hua.service.SetMealService;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author ：hua
 * @date ：Created in 2019/6/19
 * @description ：
 * @version: 1.0
 */
@RestController
@RequestMapping("/report")
public class ReportController {

    @Reference
    MemberService memberService;

    @Reference
    SetMealService setMealService;

    @Reference
    ReportService reportService;

    @RequestMapping("/getMemberReportByConditions")
    public Result getMemberReportByConditions(String[] time){
        Map<String,Object> map = new HashMap<>();
        String startTime = time[0];
        String endTime = time[1];
        int startYear = Integer.parseInt(startTime.split("-")[0]) ;
        int endYear = Integer.parseInt(endTime.split("-")[0]);
        int startMonth = Integer.parseInt(startTime.split("-")[1]);
        int endMonth = Integer.parseInt(endTime.split("-")[1]);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
        Date date1 = null;
        try {
            date1 = sdf.parse(startTime);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Calendar calendar1 = Calendar.getInstance();
        calendar1.setTime(date1);

        //创建日期列表
       List<String> months = new ArrayList<>();
            for (int i = 0;i<((endYear-startYear)*12+(endMonth-startMonth)); i++){
                SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM");
                String format = sdf2.format(calendar1.getTime());
                months.add(format);
                //当前月加1
                calendar1.add(Calendar.MONTH, 1);
            }
                map.put("months",months);
        //统计每月会员的增长数量
        List<Integer> memberCount = memberService.getReportMemberCount(months);
        //把需要展示数据的月份的会员数量添加map中
        map.put("memberCount",memberCount);
        return new Result(true, MessageConstant.GET_MEMBER_NUMBER_REPORT_FAIL, map);

    }

    @RequestMapping("/getMemberReport")
    public Result getMemberReport(){
        //创建Map集合
        Map<String,Object> map = new HashMap<>();
        //创建日期列表
        List<String> months = new ArrayList<>();
        Calendar calendar = Calendar.getInstance();
        //向前退12个月
        calendar.add(Calendar.MONTH, -12);
        for (int i = 0; i < 12; i++) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
            String format = sdf.format(calendar.getTime());
            months.add(format);
            //当前月加1
            calendar.add(Calendar.MONTH, 1);
        }
        //把需要展示数据的月份的会员数量添加map中
        map.put("months",months);
        //统计每月会员的增长数量
        List<Integer> memberCount = memberService.getReportMemberCount(months);
        //把需要展示数据的月份的会员数量添加map中
        map.put("memberCount",memberCount);
        return new Result(true, MessageConstant.GET_MEMBER_NUMBER_REPORT_FAIL, map);


        //      System.out.println("****************+++++++++++++");@RequestBody Map<String,Object> map2
        //     System.out.println(map2.get("startDate").toString());
//        Map<String,Object> map = new HashMap<>();
//
//        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
//
//        String format1 = sdf.format(map2.get("startDate"));
//        String format2 = sdf.format(map2.get("endDate"));
//        int startYear = Integer.parseInt(format1.split("-")[0]) ;
//        int endYear = Integer.parseInt(format2.split("-")[0]);
//        int startMonth = Integer.parseInt(format2.split("-")[1]);
//        int endMonth = Integer.parseInt(format2.split("-")[1]);
//
//        //获取日期
//       Calendar calendar1 = (Calendar) map2.get("startDate");
//       Calendar calendar2 = (Calendar) map2.get("endDate");
//        //创建日期列表
//       List<String> months = new ArrayList<>();
//            for (int i = 0;i<((endYear-startYear)*12+(endMonth-startMonth)); i++){
//                SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM");
//                String format = sdf2.format(calendar1.getTime());
//                months.add(format);
//                //当前月加1
//                calendar1.add(Calendar.MONTH, 1);
//            }
//                map.put("months",months);
//            //统计开始月份之前的会员数量
        // calendar1.add(Calendar.MONTH, -1);
        //                String format3 = sdf2.format(calendar1.getTime());
//        int memberCount1 = memberService.findStartDateMember(format3);
//        //统计每月会员的增长数量

//        List<Integer> memberCount2 = memberService.getReportMemberCount(months);
//        List<Integer> memberCount = new ArrayList<>();
//        for (Integer eachMonthMember : memberCount2) {
//            memberCount.add(eachMonthMember-memberCount1);
//        }
//        //把需要展示数据的月份的会员数量添加map中
//        map.put("memberCount",memberCount);
//        return new Result(true, MessageConstant.GET_MEMBER_NUMBER_REPORT_FAIL, map);


    }

    /**
     * 套餐预约占比统计
     * @return
     */
    @RequestMapping("/getSetmealReport")
    public Result getSetmealReport(){
        try {
            Map<String, Object> map = new HashMap<>();

            //把所有的套餐名称存储到一个list集合中
            List<String> setMealName = new ArrayList<>();

            //获取套餐以及对应的数量
            List<Map<String,String>> setmealCount = setMealService.findSetMealCount();
            for (Map<String, String> setMeal : setmealCount) {
                String name = setMeal.get("name");
                setMealName.add(name);

            }
            map.put("setmealNames", setMealName);
            map.put("setmealCount", setmealCount);
            return new Result(true, MessageConstant.GET_SETMEAL_COUNT_REPORT_SUCCESS, map);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.GET_SETMEAL_COUNT_REPORT_FAIL);
        }

    }

    /**
     * 运营数据统计
     * @return
     */
    @RequestMapping("/getBusinessReportData")
    public Result getBusinessReportData(){
        try {
            Map<String,Object> map = reportService.getBusinessReportData();
            return new Result(true, MessageConstant.GET_BUSINESS_REPORT_SUCCESS, map);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.GET_BUSINESS_REPORT_FAIL);
        }

    }

    /**
     * 到处excel
     * @return
     */
    @RequestMapping("/exportBusinessReport")
    public Result exportBusinessReport(HttpServletRequest request, HttpServletResponse response){
        try {
            //获取运营统计数据
            Map<String, Object> businessReportData = reportService.getBusinessReportData();
            //获取模板绝对路径
            String reportExcel = request.getSession().getServletContext().getRealPath("/template")+File.separator+"report_template.xlsx";
            //获取xlsx流
            FileInputStream inputStream = new FileInputStream(new File(reportExcel));
            //创建工作簿
            XSSFWorkbook xssfWorkbook = new XSSFWorkbook(inputStream);
            //获取sheet页
            XSSFSheet sheet = xssfWorkbook.getSheetAt(0);
            //获取行
            XSSFRow row = sheet.getRow(2);
            //获取单元格并添加数据
            //获取统计日期
            Object reportDate = businessReportData.get("reportDate");
            row.getCell(5).setCellValue(String.valueOf(reportDate));

            //获取行
             row = sheet.getRow(4);
            //获取新增会员数
            Object todayNewMember = businessReportData.get("todayNewMember");
            row.getCell(5).setCellValue(String.valueOf(todayNewMember));
            //获取会员总数
            Object totalMember = businessReportData.get("totalMember");
            row.getCell(7).setCellValue(String.valueOf(totalMember));

            //获取行
            row = sheet.getRow(5);
            //获取本周新增会员数
            Object thisWeekNewMember = businessReportData.get("thisWeekNewMember");
            row.getCell(5).setCellValue(String.valueOf(thisWeekNewMember));
            //获取本月新增会员数
            Object thisMonthNewMember = businessReportData.get("thisMonthNewMember");
            row.getCell(7).setCellValue(String.valueOf(thisMonthNewMember));

            //获取行
            row = sheet.getRow(7);
            //获取今日预约数
            Object todayOrderNumber = businessReportData.get("todayOrderNumber");
            row.getCell(5).setCellValue(String.valueOf(todayOrderNumber));
            //获取今日到诊数
            Object todayVisitsNumber = businessReportData.get("todayVisitsNumber");
            row.getCell(7).setCellValue(String.valueOf(todayVisitsNumber));
            //获取行
            row = sheet.getRow(8);
            //获取本周预约数
            Object thisWeekOrderNumber = businessReportData.get("thisWeekOrderNumber");
            row.getCell(5).setCellValue(String.valueOf(thisWeekOrderNumber));
            //获取本周到诊数
            Object thisWeekVisitsNumber = businessReportData.get("thisWeekVisitsNumber");
            row.getCell(7).setCellValue(String.valueOf(thisWeekVisitsNumber));
            //获取行
            row = sheet.getRow(9);
            //获取本月预约数
            Object thisMonthOrderNumber = businessReportData.get("thisMonthOrderNumber");
            row.getCell(5).setCellValue(String.valueOf(thisMonthOrderNumber));
            //获取本月到诊数
            Object thisMonthVisitsNumber = businessReportData.get("thisMonthVisitsNumber");
            row.getCell(7).setCellValue(String.valueOf(thisMonthVisitsNumber));


            //热门套餐
            List<Map<String,Object>> hotSetmeal = ( List<Map<String,Object>>)businessReportData.get("hotSetmeal");
            //行数
            int rowNum = 12 ;
            for (Map<String, Object> map : hotSetmeal) {
                //获取行
                row = sheet.getRow(rowNum);
                //套餐名称
                row.getCell(4).setCellValue(String.valueOf(map.get("name")));
                //预约数量
                row.getCell(5).setCellValue(String.valueOf(map.get("setmeal_count")));
                //占比
                row.getCell(6).setCellValue(String.valueOf(map.get("proportion")));
                //行数增加 1
                rowNum++;
            }

            //以附件的形式下载
            //通过输出流进行文件下载
            ServletOutputStream out = response.getOutputStream();
            //响应的类型为excel
            response.setContentType("application/vnd.ms-excel");
            //attachment --- 作为附件下载
            //filename -- 指定文件名
            response.setHeader("content-Disposition", "attachment;filename=report.xlsx");
            xssfWorkbook.write(out);
            out.flush();
            out.close();
            xssfWorkbook.close();


        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.GET_BUSINESS_REPORT_FAIL);
        }
        return null;
    }
}
