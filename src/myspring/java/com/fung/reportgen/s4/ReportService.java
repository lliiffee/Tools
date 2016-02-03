package com.fung.reportgen.s4;

import java.time.Month;
import java.util.Date;

import com.fung.reportgen.base.ReportGenerator;
import com.fung.reportgen.base.Table;

public class ReportService {

	// private static ReportGenerator generator = new PDFGenerator();
    // 消除上面的紧耦合关系，由容器取而代之
    // private ReportGenerator generator = (ReportGenerator) Container
    //         .getBean("reportGenerator");   
    // 去除上面的“主动”查找，提供私有字段来保存外部注入的对象
    private ReportGenerator generator;   
    // 以 setter 方式从外部注入
    public void setReportGenerator(ReportGenerator generator) {
       System.out.println("4...开始注入 ReportGenerator ...");
       this.generator = generator;
    }
 
    private Table table = new Table(); 
    public ReportService() {
       System.out.println("3...开始初始化 ReportService ...");
    } 
    public void getDailyReport(Date date) {
       table.setDate(date);
       generator.generate(table);
    } 
    public void getMonthlyReport(Month month) {
       table.setMonth(month);
       generator.generate(table);
    }
}
