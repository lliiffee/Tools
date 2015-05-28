package com.fung.concurrent.patern.threadPerMsg;

public class Main {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		System.out.println("main begin...");
		Host host=new Host();
		host.request(10,'A');
		
		System.out.println("main end...");

	}

}
