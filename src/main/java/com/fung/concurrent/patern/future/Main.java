package com.fung.concurrent.patern.future;

 

public class Main {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		System.out.println("main begin");
		Host host =new Host();
		Data data1=host.request(10, 'A');
		Data data2=host.request(20, 'B');
		Data data3=host.request(30, 'C');
		System.out.println("main otherJob begin");
		try{
			
			Thread.sleep(2000);
			
		}catch(Exception e)
		{
			
		}
		
		System.out.println("main otherJob end");
		System.out.println("data1="+data1.getContent());
		System.out.println("data1="+data2.getContent());
		System.out.println("data1="+data3.getContent());
		System.out.println("main end");
	}

}
