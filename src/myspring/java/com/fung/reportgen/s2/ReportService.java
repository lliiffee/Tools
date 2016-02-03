package com.fung.reportgen.s2;


import java.time.Month;
import java.util.Date;

import com.fung.reportgen.base.ReportGenerator;
import com.fung.reportgen.base.Table;

public class ReportService {
	// 消除紧耦合关系，由容器取而代之 
    // private static ReportGenerator generator = new PDFGenerator(); 
    private ReportGenerator generator = (ReportGenerator) Container.getBean("reportGenerator"); 
 
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

/*
问题描述：
然而，观察上面的类图，很容易发现 ReportService 与 Container 之间存在双向关联，彼此互相有依赖关系。并且，如果想要重用 ReportService，
由于它也是直接依赖于单独一个 Container 的具体查找逻辑。若其他容器具体不同的组件查找机制（如 JNDI），
此时重用 ReportService 意味着需要修改 Container 的内部查找逻辑。
 
解决方案：引入 Service Locator
再次引入一个间接层 Service Locator，用于提供组件查找逻辑的接口，请看Wikipedia 中的描述 或者 Java EE 对其的描述1 、描述2 。这样就能够将可能变化的点隔离开来。
*/