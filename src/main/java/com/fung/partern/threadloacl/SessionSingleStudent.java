package com.fung.partern.threadloacl;
 
//将thread local 封装在业务bean里面更容易使用。
public class SessionSingleStudent {  
	
	private static final ThreadLocal<SessionSingleStudent>session=new ThreadLocal<SessionSingleStudent>();
	
	private SessionSingleStudent(){
		
	};
	public static  /*synchronized*/ SessionSingleStudent getInstance(){
		SessionSingleStudent instance=session.get();
		if(instance==null)
		{
			instance=new SessionSingleStudent();
			session.set(instance);
		}
		return instance;
	}
	
    private int age = 0;   //年龄  
   
    public int getAge() {  
        return this.age;  
    }  
   
    public void setAge(int age) {  
        this.age = age;  
    }  
}  