package com.feng.mock;

public class SystemEnvironment implements Environmental{
	 
	
	public long getTime() {
		// TODO Auto-generated method stub
		return System.currentTimeMillis();
	}
	
	public void playWayFile(String fileName){
		System.out.println("SystemEnvironment play");
	}
 

}
