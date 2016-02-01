package com.fung.concurrent.callable;

import java.util.Random;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;

public class CallableAndFuture {
	public static void main(String[] args) {
		Callable<Integer> callable = new Callable<Integer>() {
			public Integer call() throws Exception {
				int rt=new Random().nextInt(100);
				System.out.println("子线程获得结果： "+rt);
				return rt;
			}
		};
		FutureTask<Integer> future = new FutureTask<Integer>(callable);
		new Thread(future).start();
		try {
			System.out.println("主线程在做其他东西  do sth......");
			Thread.sleep(5000);// 可能做一些事情
			System.out.println("主线程获取结果："+future.get());
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (ExecutionException e) {
			e.printStackTrace();
		}
		
		test2();
	}
	
	
	public static void test2(){
		 ExecutorService threadPool = Executors.newSingleThreadExecutor();  
	        Future<Integer> future = threadPool.submit(new Callable<Integer>() {  
	            public Integer call() throws Exception {  
	                return new Random().nextInt(100);  
	            }  
	        });  
	        try {  
	            Thread.sleep(5000);// 可能做一些事情  
	            System.out.println(future.get());  
	        } catch (InterruptedException e) {  
	            e.printStackTrace();  
	        } catch (ExecutionException e) {  
	            e.printStackTrace();  
	        }  
	}
}