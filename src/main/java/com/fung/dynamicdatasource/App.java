package com.fung.dynamicdatasource;

import java.sql.ResultSet;
import java.sql.Statement;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

public class App {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

		doTask();
	}
	
	public static void doTask()
	{

		String[] springConfig  = 
			{	
				"dynamic_datasource.xml" 
			};
		
		ApplicationContext ctx = 
				new ClassPathXmlApplicationContext(springConfig);
		DataSourceContextHolder.setDataSourceType(DataSourceConst.TEST);
		 
		AbstractRoutingDataSource sr=(AbstractRoutingDataSource)ctx.getBean("dataSource");
		try{
			Statement st= sr.getConnection().createStatement();
			ResultSet rs=st.executeQuery("select * from tb_content");
			if(rs.next())
			System.out.println(rs.getString(1));
		}catch(Exception e)
		{
			e.printStackTrace();
		}
		
		
		DataSourceContextHolder.setDataSourceType(DataSourceConst.USER);
		  
		try{
			Statement st= sr.getConnection().createStatement();
			ResultSet rs=st.executeQuery("select * from ts_user");
			if(rs.next())
			System.out.println(rs.getString(1));
		}catch(Exception e)
		{
			e.printStackTrace();
		}
		
	  
		System.out.println(DataSourceContextHolder.getDataSourceType());
	}

}

//http://blog.csdn.net/llhwin2010/article/details/11695781
//http://www.oschina.net/question/239572_118084
//http://blog.csdn.net/shadowsick/article/details/8878448
//http://zy116494718.iteye.com/blog/1830138
//http://phl.iteye.com/blog/1983114
