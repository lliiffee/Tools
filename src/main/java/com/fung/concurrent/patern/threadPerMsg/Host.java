package com.fung.concurrent.patern.threadPerMsg;

public class Host {

	private final Helper helper=new Helper();
	
	//新线程，这个工作交给你了。。
	public void request(final int count, final char c) {
	    System.out.println("   request ("+count+", "+c+")begin");
	    
	    new Thread(){
	    	public void run(){
	    		helper.handle(count,c);
	    	}
	    }.start();
		
	    System.out.println("   request ("+count+", "+c+")end");
	}

}
