package com.fung.concurrent.patern.gardsuspend;

public class Main {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		RequestQueue queue=new RequestQueue();
		new ClientThread(queue,"alice",31455932).start();
		new ServerThread(queue,"bbbbbb",35663557).start();
		new ServerThread(queue,"ffffff",35663556).start();
	}

}
