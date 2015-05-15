package com.fung.concurrent.patern.gardsuspend;

import java.util.Random;

public class ClientThread extends Thread {

	private Random random;
	private RequestQueue requestQueue;
	public ClientThread(  RequestQueue requestQueue,String name, long seed) {
		super(name);
		this.random = new Random(seed);
		this.requestQueue = requestQueue;
	}
	@Override
	public void run() {
		for(int i=0;i<10000;i++)
		{
			Request request=new Request("No. "+i);
			System.out.println(Thread.currentThread().getName() +" request "+request);
			requestQueue.putRequest(request);
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 
		}
	}
	
	
	 

}
