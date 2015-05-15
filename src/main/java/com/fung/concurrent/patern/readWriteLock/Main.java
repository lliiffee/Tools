package com.fung.concurrent.patern.readWriteLock;

public class Main {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

		Data data=new Data(10);
		new ReaderThread(data).start();
		new ReaderThread(data).start();
		new ReaderThread(data).start();
		new WriterThread(data,"ABCDEFG").start();
		new WriterThread(data,"abcdefg").start();
		
	}

}
