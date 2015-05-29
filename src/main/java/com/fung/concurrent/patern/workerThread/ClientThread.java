package com.fung.concurrent.patern.workerThread;

import java.util.Random;

public class ClientThread extends Thread{

	private Channel channel;
	private static final Random random=new Random();
	public ClientThread(String name, Channel channel) {
		super(name);
		this.channel=channel;
	}

	public void run(){
		try{
			for(int i=0;true;i++)
			{
				Request request=new Request(getName(),i);
				channel.putRequest(request);
				Thread.sleep(random.nextInt(1000));
			}
		}catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	
}
