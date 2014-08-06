package com.fung.partern.threadloacl;

public class OneThreadLocalTest{

	public static final ThreadLocal<DemoBean>session=new ThreadLocal<DemoBean>();
	public static DemoBean currentSession(){
		DemoBean bean=(DemoBean)session.get();
		if(bean==null){
			bean=new DemoBean();
	    bean.setName("");
	      System.out.println(bean.getName());
		 session.set(bean);
		}
		 return bean;
	}
	
	
}
