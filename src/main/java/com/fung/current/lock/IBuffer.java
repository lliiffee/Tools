package com.fung.current.lock;

public interface IBuffer {
	 public void write();  
	    public void read() throws InterruptedException;  
}
