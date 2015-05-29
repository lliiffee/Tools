package com.fung.concurrent.patern.workerThread;

public class Main {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Channel channel=new Channel(5);
		channel.startWorks();
		new ClientThread("A",channel).start();
		new ClientThread("b",channel).start();
		new ClientThread("c",channel).start();
		new ClientThread("A2",channel).start();
		new ClientThread("b2",channel).start();
		new ClientThread("c2",channel).start();


	}

}
