package com.fung.generic;

import java.nio.channels.Selector;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

 
 

public class Test {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
          Map m=null;
          try {
        	  
			List<Integer> l = getRecords(Integer.class);
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
	}
	
	/*
	 * 编译器将会根据 FooRecord.class 是 Class<FooRecord> 类型的这一事实，推断 getRecords() 
	 * 的返回类型。您使用类常量来构造新的实例并提供编译器在类型检查中要用到的类型信息。
	 */
	public static<T> List<T> getRecords(Class<T> c) throws Exception { 
		// Use Selector to select rows 
		List<T> list = new ArrayList<T>(); 

		for (int i=0;i<10;i++) { 
		    T row = c.newInstance(); 
		    // use reflection to set fields from result 
		    list.add(row); 
		} 
		return list; 
		} 
	

}
