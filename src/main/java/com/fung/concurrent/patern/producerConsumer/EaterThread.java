package com.fung.concurrent.patern.producerConsumer;

import java.util.Random;

public class EaterThread extends Thread {
	
	private final Random random;
	private final Table table;
	
	public EaterThread(String name,long seed, Table table) {
		super(name);
		this.random = new Random(seed);
		this.table = table;
	}
	
	public void run(){
		try{
			while(true){
			String cake=table.take();
			Thread.sleep(random.nextInt(1000));
			}
		}catch(Exception e)
		{
			e.printStackTrace();
		}
	}

}
