package com.fung.concurrent.patern.gardsuspend;

import java.util.LinkedList;

public class RequestQueue {

	 private final LinkedList queue=new LinkedList();
	 public synchronized Request getRequest()
	 {
		 System.out.println(Thread.currentThread().getName()+".. in ..");
		 while(queue.size()<=0)//如果是多个线程在wait 用if ..只会第一个检查条件？ 后面的就不检查，直接运行removeFirst....所以要用while..
		 {//notify and notifyAll 唤醒的线程 会前进到 wait的下一个语句，会获取类的实例。
			 //interruct 是Thread 方法。。会抛出 InterruptedException, nterruput 唤醒会不会需要获取实例所呢？？？？ 
			 try{
				 System.out.println(Thread.currentThread().getName()+"..sleep ..");
				 wait();
			 }catch(InterruptedException e){
				 
			 }
		 }
		 System.out.println(Thread.currentThread().getName()+".. after ..");
		 return (Request)queue.removeFirst();
	 }
	 
	 public synchronized void putRequest(Request request)
	 {
		 queue.addLast(request);
		 notifyAll();
	 }
}
