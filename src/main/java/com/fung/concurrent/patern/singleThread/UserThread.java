package com.fung.concurrent.patern.singleThread;

public class UserThread extends Thread{
	private final Gate gate;
	private final String myName;
	private final String myAddress;
	
	public UserThread(Gate gate,String name,String address)
	{
		this.gate=gate;
		this.myName=name;
		this.myAddress=address;
	}
	
	public void run(){
		System.out.println(myName+" begin ");
		for(int i=0;i<1000;i++)
		{
			gate.pass(myName, myAddress);
		}
	}

}
