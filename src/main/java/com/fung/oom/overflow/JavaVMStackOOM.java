package com.fung.oom.overflow;

public class JavaVMStackOOM {

	/**
	 * vm args  -Xss2M  
	 */
	//创建线程溢出。。。
	public static void main(String[] args) {
		JavaVMStackOOM jo=new JavaVMStackOOM();
		jo.statckLeakByThread();

	}
	
	private void dontStop()
	{
		while(true)
		{
			
		}
	}
	
	public void statckLeakByThread(){
		while(true)
		{
			Thread t=new Thread(
					   new Runnable(){
						   public void run(){
							   dontStop();
						   }
					   });
			t.start();
		}
	}

}
//OOM:unable to create new native thread