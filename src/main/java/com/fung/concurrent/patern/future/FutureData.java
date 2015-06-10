package com.fung.concurrent.patern.future;

public class FutureData implements Data{

	private RealData realdata=null;
	private boolean ready=false;
	
	public synchronized void setRealData(RealData realdata) {
		if(ready){
			return;
		}
		this.realdata=realdata;
		this.ready=true;
		notifyAll();
	}

	public synchronized String getContent() {
		 while(!ready){
			  try {
				wait();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 
		 }
		 return realdata.getContent();
	}

}
