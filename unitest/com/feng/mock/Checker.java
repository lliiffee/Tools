package com.feng.mock;

import java.util.Calendar;

public class Checker {
	private Environmental env;
	private boolean flag=false;
	
	public Checker(Environmental aenv){
		
		this.env=aenv;
	}
	public boolean wavWasPlayed()
	{
		return flag;
	}
	
	public void reset()
	{
		this.flag=false;
	}
	public void reminder(){
		Calendar cal=Calendar.getInstance();
		cal.setTimeInMillis(env.getTime());
		int hour=cal.get(Calendar.HOUR_OF_DAY);
		if(hour>=17)
		{
			env.playWayFile("quet_whistle.wav");
			flag=true;
		}
	}

}
