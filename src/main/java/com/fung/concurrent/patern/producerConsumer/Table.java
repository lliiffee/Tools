package com.fung.concurrent.patern.producerConsumer;

public class Table {
	
	private final String[] buffer;
	private int tail; //下一个put 的地方
	private int head;//下一个take的地方
	private int count;//buffer内的蛋糕数
	
	

	public Table(int count) {
		this.buffer=new String[count];
		this.count = count;
		this.head=0;
		this.tail=0;
		this.count=0;
	}

    public synchronized void put(String cake) throws InterruptedException
    {
    	
    	while(count>=buffer.length){
    		System.out.println(Thread.currentThread().getName()+" sleep...zzzz.");
    		wait();
    	}
    	buffer[tail]=cake;
    	System.out.println(Thread.currentThread().getName()+" puts "+cake +" ###pu to tail="+tail);
    	tail=(tail+1)% buffer.length; //位置跳去下一个，如果到结尾 返回开始位置。
    	 /*
  	   * 上面的相当于  tail++ ; if(tail >=buffer.length) {tail=0;} 
  	   */
    	System.out.println(Thread.currentThread().getName()+" puts "+cake +" ###change tail="+tail);
    	count++;
    	notifyAll();
    }
	public synchronized String take() throws InterruptedException {
		while(count<=0)
		{
			System.out.println(Thread.currentThread().getName()+" sleep...zzzz.");
    		wait();
		}
		String cake=buffer[head];
		System.out.println(Thread.currentThread().getName()+" takes "+cake+"$$$get from head="+head);
		head=(head+1) % buffer.length;
	 
		count--;
		notifyAll();
		System.out.println(Thread.currentThread().getName()+" takes "+cake+"$$$change head="+head);
		return cake;
	}

}
