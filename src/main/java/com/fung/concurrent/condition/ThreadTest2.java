package com.fung.concurrent.condition;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
/*
 * 接近一周没更新《Java线程》专栏了，主要是这周工作上比较忙，生活上也比较忙，呵呵，进入正题，上一篇讲述了并发包下的Lock，Lock可以更好的解决线程同步问题，
 * 使之更面向对象，并且ReadWriteLock在处理同步时更强大，那么同样，线程间仅仅互斥是不够的，还需要通信，本篇的内容是基于上篇之上，使用Lock如何处理线程通信。
        那么引入本篇的主角，Condition，Condition 将 Object 监视器方法（wait、notify 和 notifyAll）分解成截然不同的对象，
        以便通过将这些对象与任意 Lock 实现组合使用，为每个对象提供多个等待 set （wait-set）。其中，Lock 替代了 synchronized 方法和语句的使用，
        Condition 替代了 Object 监视器方法的使用。下面将之前写过的一个线程通信的例子替换成用Condition实现(Java线程(三))，代码如下
 */
public class ThreadTest2 {
	public static void main(String[] args) {
		final Business business = new Business();
		new Thread(new Runnable() {
			@Override
			public void run() {
				threadExecute(business, "sub");
			}
		}).start();
		threadExecute(business, "main");
	}	
	public static void threadExecute(Business business, String threadType) {
		for(int i = 0; i < 2; i++) {
			try {
				if("main".equals(threadType)) {
					business.main(i);
				} else {
					business.sub(i);
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}
class Business {
	private boolean bool = true;
	private Lock lock = new ReentrantLock();
	private Condition condition = lock.newCondition(); 
	public /*synchronized*/ void main(int loop) throws InterruptedException {
		lock.lock();
		try {
			while(bool) {				
				condition.await();//this.wait();
			}
			for(int i = 0; i < 10; i++) {
				System.out.println("main thread seq of " + i + ", loop of " + loop);
			}
			bool = true;
			condition.signal();//this.notify();
		} finally {
			lock.unlock();
		}
	}	
	public /*synchronized*/ void sub(int loop) throws InterruptedException {
		lock.lock();
		try {
			while(!bool) {
				condition.await();//this.wait();
			}
			for(int i = 0; i < 10; i++) {
				System.out.println("sub thread seq of " + i + ", loop of " + loop);
			}
			bool = false;
			condition.signal();//this.notify();
		} finally {
			lock.unlock();
		}
	}
}
