package com.fung.concurrent.lock;

public interface IBuffer {
	 public void write();  
	    public void read() throws InterruptedException;  
}
