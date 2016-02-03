package com.fung.reportgen.s3;



import com.fung.reportgen.base.ReportGenerator;
import com.fung.reportgen.s2.Container;

//实际应用中可以是用 interface 来提供统一接口 
class ServiceLocator { 
 private static Container container = new Container(); 
  
 public static ReportGenerator getReportGenerator() { 
     return (ReportGenerator)container.getBean("reportGeneraator"); 
 } 
 

} 
