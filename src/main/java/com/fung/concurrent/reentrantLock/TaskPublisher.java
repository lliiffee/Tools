package com.fung.concurrent.reentrantLock;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class TaskPublisher extends Thread {
	TaskExtWithReentrantLock lock;
	private int id;

	public TaskPublisher(int i, TaskExtWithReentrantLock test) {
		this.id = i;
		this.lock = test;
	}

	public void run() {
		lock.print(id);   //要做的事情交给有lock的对象处理,他会根据lock 的分配进行控制
	}

	public static void main(String args[]) {
		ExecutorService service = Executors.newCachedThreadPool();
		TaskExtWithReentrantLock lock = new TaskExtWithReentrantLock();  //#####只有一个....
		for (int i = 0; i < 10; i++) {
			service.submit(new TaskPublisher(i, lock));
		}
		service.shutdown();
	}
}