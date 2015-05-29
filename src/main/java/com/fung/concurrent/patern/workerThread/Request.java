package com.fung.concurrent.patern.workerThread;

public class Request {

	private final String name;
	private final int number;
	
	public Request(String name, int i) {
		this.name=name;
		this.number=i;
	}
	
	public void execute(){
		System.out.println(Thread.currentThread().getName()+" executes " +this );
		try{
			Thread.sleep(1000);
		}catch(InterruptedException e)
		{
			
		}
		
		
	}

	@Override
	public String toString() {
		return "Request [name=" + name + ", number=" + number + "]";
	}

}
