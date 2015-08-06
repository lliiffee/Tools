package com.fung.patern.nimpus;

public class NibusOneTemperatureSensorImpl extends TemperatureSensor{

	@Override
	public double read() {
		// TODO Auto-generated method stub
		System.out.println("read from C api.....");
		return 10;
	}
	
	

}
