package com.fung.concurrent.patern.future;

public class RealData  implements Data {
	
	private final String content;

	public RealData(int count, char c) {
		System.out.println(" making realdata ("+count+","+c+") begin");
		char[] buffer=new char[count];
		for(int i=0;i<count;i++)
		{
			buffer[i]=c;
		}
		 try {
			Thread.sleep(100);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println(" making realdata ("+count+","+c+") end");
		this.content=new String(buffer);
	}

	public String getContent() {
		
		return content;
	}

}
