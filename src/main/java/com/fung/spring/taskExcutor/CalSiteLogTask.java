package com.fung.spring.taskExcutor;

import org.springframework.core.task.TaskExecutor;

public class CalSiteLogTask implements Runnable {

	 
	 

	public void run() {
		// TODO Auto-generated method stub
		
		for(int i=0;i<100;i++)
		{
			System.out.println("-->"+i);
		}

	}

}
