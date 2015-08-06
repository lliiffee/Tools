package com.fung.patern.nimpus;

import java.util.Observable;

public abstract class TemperatureSensor extends Observable {
	
	private double itsLastReading;
	
	AlarmClock alarmClock;
	
	public TemperatureSensor(AlarmClock alarmClock){
		this.alarmClock=alarmClock;
		alarmClock.wakeUp(10000, new AlarmListener(){
			public void wakeup(){
				check();
			}
		});
	}
	
	private   void check(){
		double val=read();
		if(val!=this.itsLastReading)
		{
			itsLastReading=val;
			setChanged();
			notifyObservers(val);
		}
	}
	
	public void wakeUp()
	{
		check();
	}
	public abstract double read();

}
