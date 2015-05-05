package com.feng.mock;

public class MockSystemEnvironment implements Environmental{
	private long current_time;

	public long getTime() {
		// TODO Auto-generated method stub
		return current_time;
	}
	
	public void setTime(long aTime){
		current_time=aTime;
	}
	
	public void playWayFile(String fileName){
		System.out.println("MockSystemEnvironment play");
	}

}