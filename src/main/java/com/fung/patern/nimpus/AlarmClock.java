package com.fung.patern.nimpus;

public interface AlarmClock {

	public void wakeUp(int interval,AlarmListener al);

	public void register(int i, AlarmListener alarmListener);
}
