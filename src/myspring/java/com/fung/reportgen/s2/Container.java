package com.fung.reportgen.s2;

import java.util.HashMap;
import java.util.Map;

import com.fung.reportgen.base.PDFGenerator;
import com.fung.reportgen.base.ReportGenerator;

public class Container
{
//以键-值对形式保存各种所需组件 Bean 
private static Map<String, Object> beans; 
 
public Container() { 
    beans = new HashMap<String, Object>(); 
     
    // 创建、保存具体的报表生起器 
    ReportGenerator reportGenerator = new PDFGenerator(); 
    beans.put("reportGenerator", reportGenerator); 
     
    // 获取、管理 ReportService 的引用 
    ReportService reportService = new ReportService(); 
    beans.put("reportService", reportService); 
} 
 
	public static Object getBean(String id) { 
	    return beans.get(id); 
	} 
}
