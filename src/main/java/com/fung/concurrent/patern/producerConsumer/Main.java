package com.fung.concurrent.patern.producerConsumer;

public class Main {
	
	public static void main(String[] args){
		Table table=new Table(3);
		new MakerThread("M1",3145,table).start();
		new MakerThread("M2",3146,table).start();
		new MakerThread("M3",3147,table).start();
		
		new EaterThread("E1",3148,table).start();
		new EaterThread("E2",3149,table).start();
	}

	//参与制 cake
	//生产者
	//消费者
	//通道
}
