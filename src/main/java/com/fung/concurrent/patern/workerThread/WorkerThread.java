package com.fung.concurrent.patern.workerThread;

public class WorkerThread extends Thread{

	private final Channel channel;
	public WorkerThread(String name, Channel channel) {
		// TODO Auto-generated constructor stub
		super(name);
		this.channel=channel;
	}

	
	public void run(){
		while(true)
		{
			Request request=channel.takeRequest();
			request.execute();
		}
	}
}
