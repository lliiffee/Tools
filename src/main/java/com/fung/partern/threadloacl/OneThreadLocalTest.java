package com.fung.partern.threadloacl;

public class OneThreadLocalTest{

	public static final ThreadLocal<DemoBean>session=new ThreadLocal<DemoBean>();
	public static DemoBean currentSession(){
		DemoBean bean=(DemoBean)session.get();
		if(bean==null){
			bean=new DemoBean();
		 session.set(bean);
		 
		}
		 return bean;
	}
	
	
	// session 里面， 根据 每个Thread   Map (<t1> :  ThreadLocalMap <A : bean>   )   所以每个线程操作的都是自己对应map 里面的对象
	/*
	Map { <t1> :  ThreadLocalMap {<t1> : bean(A)} , 
	      <t2> :  ThreadLocalMap {<t2> : bean(B) }     
	  }
	  */
	 
	
	
	public static void main(String args[])
	{
      
    	  Thread t=new Thread(){
  			public void run(){
  				DemoBean bean= currentSession(); 
  				bean.setName("A");
  				try {
					Thread.sleep(4000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
  				System.out.println(bean.getName());
  			}
  		};
  		t.start();
  		

  		 Thread t2=new Thread(){
   			public void run(){
   				DemoBean bean= currentSession(); 
   				bean.setName("B");
   				
   				try {
					Thread.sleep(4000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
  				System.out.println(bean.getName());
   			}
   		};
   		t2.start();
		
	}
	
}
