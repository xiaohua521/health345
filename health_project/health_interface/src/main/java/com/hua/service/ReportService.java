package com.hua.service;

import java.util.List;
import java.util.Map;

public interface ReportService {
    /**
     * 统计运营数据
     * @return
     */
    Map<String, Object> getBusinessReportData() throws Exception;

    //查询男女比例
    List<Map<String, String>> getSexproportion();

    //查询年龄段
    List<Map<String, String>> getAgebracket();
}
