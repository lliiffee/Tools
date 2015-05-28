package com.fung.concurrent.patern.readWriteLock;

public class ReadWriteLock {
	
	/*读取 和 写入 冲突
	  写入和写入
	  
	  read..
	  (1) 已经有线程写入。等待
	  2 已经有线程在读，不等
     write
      已有写。。等
      已有读。。等

	*
	*/
	
	private int readingReaders=0;
	private int waitingWriters=0;  //writeLock时，进入wait
	private int writingWriters=0; //正在写。。
	private boolean preferWriter=true; //标志位。。写入优先的话，设置为true;

	public synchronized void readLock() throws InterruptedException {
		//如果有写线程。。或  写优先 且 有等待写的线程 。。。。等。。
		while(writingWriters>0 || preferWriter && waitingWriters>0)
		{ //gard suspension patern
			wait();
		}
		readingReaders++;//正在读 +1
	}

	public synchronized void readUnlock() {
		readingReaders--;  //正在读 -1。。
		preferWriter=true;
		notifyAll();
	}

	public synchronized void writeLock() throws InterruptedException {
		waitingWriters++; //等待写入线程 +1
		try{
			while(readingReaders>0||writingWriters>0)
			{
				wait();
			}
		}finally{
			waitingWriters--; //等待写入线程 -1
		}
		writingWriters++; //正在写入线程+1
	}

	public synchronized void writeUnlock() {
		 writingWriters--;  //正在写 -1
		 preferWriter=false;
		 notifyAll();
		
	}

}
