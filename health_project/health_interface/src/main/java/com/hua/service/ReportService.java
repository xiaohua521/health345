package com.hua.service;

import java.util.Map;

public interface ReportService {
    /**
     * 统计运营数据
     * @return
     */
    Map<String, Object> getBusinessReportData() throws Exception;
}
