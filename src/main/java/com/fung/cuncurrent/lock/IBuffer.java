package com.fung.cuncurrent.lock;

public interface IBuffer {
	 public void write();  
	    public void read() throws InterruptedException;  
}
