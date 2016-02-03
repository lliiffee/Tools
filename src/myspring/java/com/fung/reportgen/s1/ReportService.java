package com.fung.reportgen.s1;

import java.time.Month;
import java.util.Date;

import com.fung.reportgen.base.PDFGenerator;
import com.fung.reportgen.base.ReportGenerator;
import com.fung.reportgen.base.Table;

public  class ReportService { 
    // 负责创建具体需要的报表生成器 
    private ReportGenerator generator = new PDFGenerator(); 
    // private static ReportGenerator generator = new ExcelGenerator(); 
     
    public void getDailyReport(Date date) { 
    	Table table=new Table();
        table.setDate(date); 
        // ... 
        generator.generate(table); 
    } 
     
    public void getMonthlyReport(Month month) { 
    	Table table=new Table();
        table.setMonth(month); 
        // ... 
        generator.generate(table); 
    } 
} 
