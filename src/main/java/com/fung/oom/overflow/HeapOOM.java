package com.fung.oom.overflow;

import java.util.ArrayList;
import java.util.List;

/*
 * vm args : -Xms20m  -Xmx20m -XX:+HeapDumpOnOutOfMemoryError
 */
public class HeapOOM {

	static class OOMObject{
		
	}
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
               List <OOMObject> list=new ArrayList<OOMObject>();
               while(true)
               {
            	   list.add(new OOMObject());
               }
	}

}
