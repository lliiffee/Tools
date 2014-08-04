package com.fung.thread;

/*
 * 不要使用，demo 没有同步情况下使用共享变量 .. ReaderThread 可能永远都读不到 主线程设置的值 42。。
 */
public class NoVisibility {
	private static boolean ready;
	private static int number;
	
	private static class ReaderThread extends Thread{
		public void run()
		{
			while (!ready)
			{
				System.out.println("--");
				Thread.yield();
			}
			
			System.out.println(number);
		}
	}
	
	public static void main(String[] args){
		
		new ReaderThread().start();
//		try {
//			Thread.sleep(10000);
//		} catch (InterruptedException e) {
//			e.printStackTrace();
//		}
		number=42;
		ready=true;
	}

}
