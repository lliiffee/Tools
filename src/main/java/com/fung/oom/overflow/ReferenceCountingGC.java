package com.fung.oom.overflow;

public class ReferenceCountingGC {

	/*
	 * testGC 方法执行后，objA 和 objB 会不会倍GC掉呢、。
	 */
	
	public Object insance=null;
	
	private static final int _1MB=1024*1024;
	
	private byte[] bigSize=new byte[2*_1MB];
	
	public static void testGC(){
		ReferenceCountingGC objA=new ReferenceCountingGC();
		ReferenceCountingGC objB=new ReferenceCountingGC();
		objA.insance=objB;
		objB.insance=objA;
		
		objA=null;
		objB=null;
		
		System.gc();
		
		
	}
	
	public static void main(String[] args){
		testGC();
	}
}
