package com.fung.concurrent.patern.gardsuspend;

import java.util.LinkedList;

public class RequestQueue {

	 private final LinkedList queue=new LinkedList();
	 public synchronized Request getRequest()
	 {
		 System.out.println(Thread.currentThread().getName()+".. in ..");
		 while(queue.size()<=0)//如果是多个线程在wait 用if ..只会第一个检查条件？ 后面的就不检查，直接运行removeFirst....所以要用while..
		 {
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
