package com.fung.concurrent.patern.singleThread;

public class Gate {
	
	private int counter=0;
	private String name="Nobody";
	private String address="noWhere";
	
	public synchronized void  pass(String name,String address)
	{
		this.counter++;
		this.name=name;
		this.address=address;
		check(); //为什么check 不需要加？
	}
	
	public synchronized String toString(){ //为什么 toStirng要加？
		return "No."+counter+" : "+name+","+address;
	}
	
	private void check(){
		if(name.charAt(0)!=address.charAt(0)){
			System.out.println("******Broken****"+this.toString());
		}
	}

}
