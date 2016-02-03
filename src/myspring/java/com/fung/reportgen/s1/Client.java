package com.fung.reportgen.s1;

import java.util.Date;

public class Client { 
    public static void main(String[] args) { 
        ReportService reportService = new ReportService(); 
        reportService.getDailyReport(new Date()); 
        //reportService.getMonthlyReport(new Date()); 
    } 
} 

/*
问题描述：
如上面代码中的注释所示，具体的报表生成器由 ReportService 类内部硬编码创建，由此 ReportService 已经直接依赖于 PDFGenerator 或 ExcelGenerator ，必须消除这一明显的紧耦合关系。

解决方案：引入容器
引入一个中间管理者，也就是容器（Container），由其统一管理报表系统所涉及的对象（在这里是组件，我们将其称为 Bean），包括 ReportService 和各个 XXGenerator 。在这里使用一个键-值对形式的 HashMap 实例来保存这些 Bean。

*/