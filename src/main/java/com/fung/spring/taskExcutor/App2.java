package com.fung.spring.taskExcutor;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

public class App2 {
	
	
	public static void main(String[] args) { 
        ApplicationContext ctx =  
    new AnnotationConfigApplicationContext(AppConfig.class); 
        ThreadPoolTaskExecutor taskExecutor = 
    (ThreadPoolTaskExecutor)ctx.getBean("taskExecutor"); 
         
        PrintTask2 printTask1 = (PrintTask2)ctx.getBean("printTask2"); 
        printTask1.setName("Thread 1"); 
        taskExecutor.execute(printTask1); 
         
        PrintTask2 printTask2 = (PrintTask2)ctx.getBean("printTask2"); 
        printTask2.setName("Thread 2"); 
        taskExecutor.execute(printTask2); 
         
        PrintTask2 printTask3 = (PrintTask2)ctx.getBean("printTask2"); 
        printTask3.setName("Thread 3"); 
        taskExecutor.execute(printTask3); 
         
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
} 

}
