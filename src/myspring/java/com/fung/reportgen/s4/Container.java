package com.fung.reportgen.s4;

import java.util.HashMap;
import java.util.Map;

import com.fung.reportgen.base.PDFGenerator;
import com.fung.reportgen.base.ReportGenerator;

public class Container
{
	// 以键-值对形式保存各种所需组件 Bean
    private static Map<String, Object> beans; 
    public Container() {
       System.out.println("1...开始初始化 Container ..."); 
       beans = new HashMap<String, Object>(); 
       // 创建、保存具体的报表生起器
       ReportGenerator reportGenerator = new PDFGenerator();
       beans.put("reportGenerator", reportGenerator); 
       // 获取、管理 ReportService 的引用
       ReportService reportService = new ReportService();
       // 注入上面已创建的具体 ReportGenerator 实例
       reportService.setReportGenerator(reportGenerator);
       beans.put("reportService", reportService); 
       System.out.println("5...结束初始化 Container ...");
    } 
    public static Object getBean(String id) {
       System.out.println("最后获取服务组件...getBean() --> " + id + " ...");
       return beans.get(id);
    }
}
