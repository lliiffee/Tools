package com.fung.spring.taskExcutor;


public class CalSiteLogTask implements Runnable {

	 
	 

	public void run() {
		// TODO Auto-generated method stub
		
		for(int i=0;i<100;i++)
		{
			System.out.println("-->"+i);
		}

	}

}
