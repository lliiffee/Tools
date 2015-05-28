package com.fung.concurrent.patern.threadPerMsg;

public class Helper {

	public void handle(int count, char c) {
		// TODO Auto-generated method stub
		System.out.println(" handle ("+count+", "+c+")begin");
		for(int i=0;i<count;i++)
		{
			slowly();
			System.out.println(c);
		}
		
		System.out.println("");
		System.out.println(" handle ("+count+", "+c+")end");
	}
	
	private void slowly(){
		try{
			Thread.sleep(100);
		}catch(InterruptedException e)
		{
			
		}
	}

}
