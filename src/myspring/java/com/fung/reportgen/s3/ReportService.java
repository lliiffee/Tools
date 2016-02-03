package com.fung.reportgen.s3;

import java.time.Month;
import java.util.Date;

import com.fung.reportgen.base.ReportGenerator;
import com.fung.reportgen.base.Table;

public class ReportService {
	 
	// 消除紧耦合关系，由容器取而代之
    // private static ReportGenerator generator = new PDFGenerator();
    // 通过 Container..getBean("reportGenerator") ‘主动’查找
   // private ReportGenerator generator = (ReportGenerator) Container.getBean("reportGenerator");
	private ReportGenerator generator = ServiceLocator.getReportGenerator(); 
	 
	 
	 public void getDailyReport(Date date) { 
	    	Table table=new Table();
	        table.setDate(date); 
	        generator.generate(table); 
	    } 
	     
	    public void getMonthlyReport(Month month) { 
	    	Table table=new Table();
	        table.setMonth(month); 
	        generator.generate(table); 
	    } 
}
