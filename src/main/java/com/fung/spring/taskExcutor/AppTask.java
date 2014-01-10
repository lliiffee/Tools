package com.fung.spring.taskExcutor;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

public class AppTask {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		String[] springConfig  = 
			{	
				"applicationContext.xml" 
			};
	 
		ApplicationContext ctx = 
				new ClassPathXmlApplicationContext(springConfig);
	 
		ThreadPoolTaskExecutor taskExecutor = 
			    (ThreadPoolTaskExecutor)ctx.getBean("taskExecutor"); 
		taskExecutor.execute(new CalSiteLogTask());
		taskExecutor.execute(new CalSiteLogTask());
		
		 // 检查活动的线程，如果活动线程数为0则关闭线程池 
        for(;;){ 
                int count = taskExecutor.getActiveCount(); 
                System.out.println("Active Threads : " + count); 
                try{ 
                        Thread.sleep(1000); 
                }catch(InterruptedException e){ 
                        e.printStackTrace(); 
                } 
                if(count==0){ 
                        taskExecutor.shutdown(); 
                        break; 
                } 
        } 
		 
		System.out.println("done");
	}
	  
}
