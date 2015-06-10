package com.fung.concurrent.patern.future;

public class Host {

	public Data request(final int count, final char c) {
		System.out.println(" request( "+count+" , "+c+" begin");
		//1.建立future instance
		final FutureData future=new FutureData();
		//2.为了建立RealData 的实例，启动新的线程。
		new Thread(){
			public void run(){
				RealData realdata=new RealData(count ,c);
				future.setRealData(realdata);
			}
		}.start();
		System.out.println(" request( "+count+" , "+c+" end");
		//取回 FutureData 实例，作为返回值
		return future;
	}

}
