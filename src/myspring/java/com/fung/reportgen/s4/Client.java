package com.fung.reportgen.s4;

import java.util.Date;

 
import com.fung.reportgen.s2.Container;

public class Client {
	 public static void main(String[] args) { 
	        Container container = new Container(); 
	        ReportService reportService = (ReportService)Container.getBean("reportService"); 
	        reportService.getDailyReport(new Date()); 
	        //reportService.getMonthlyReport(new Date()); 
	    } 
}


/*
 * 解决方案：
在这种情况下，变‘主动’为‘被动’无疑能够减少 ReportService 的内部知识（即查找组件的逻辑）。根据控制反转（IoC）原则，可江此种拉（Pull，主动的）转化成推（Push，被动的）的模式。
 
例如，平时使用的 RSS 订阅就是Push的应用，省去了我们一天好几次登录自己喜爱的站点主动获取文章更新的麻烦。
 
而依赖注入（DI）则是实现这种被动接收、减少客户（在这里即ReportService）自身包含复杂逻辑、知晓过多的弊病。
 */
