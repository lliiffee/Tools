package com.fung.concurrent.patern.balking;

import java.io.IOException;

public class SaverThread extends Thread {
private Data data;

	public SaverThread(String name,Data data) {
	super(name);
	this.data = data;
}

	@Override
	public void run() {
		 while(true)
		 {
			 try {
				data.save();
				Thread.sleep(1000);
				 
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}catch (InterruptedException e)
			{
				e.printStackTrace();
			}
			
		 }
	}
	
}
