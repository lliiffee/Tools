package com.fung.concurrent.patern.producerConsumer;

import java.util.Random;

public class MakerThread extends Thread {
	private final Random random;
	private final Table table;
	private static int id=0; //蛋糕流水号，所有厨师相同
	public MakerThread(String name,long seed, Table table) {
		super(name);
		this.random = new Random(seed);
		this.table = table;
	}
	
	public void run(){
		try{
			while(true){
				Thread.sleep(random.nextInt(1000));
				String cake="[cake No."+nextId()+" by "+getName();
				table.put(cake);
			} 

		}catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	
	private static synchronized int nextId(){
		return id++;
	}
	
	

}
