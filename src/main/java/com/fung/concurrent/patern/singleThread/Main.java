package com.fung.concurrent.patern.singleThread;

public class Main {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		System.out.println("test gate , hit ctrl+c to exit.");
		Gate gate =new Gate();
		new UserThread(gate,"a", "a_addr").start();
		new UserThread(gate,"b", "b_addr").start();
		new UserThread(gate,"c", "c_addr").start();
	}

}
