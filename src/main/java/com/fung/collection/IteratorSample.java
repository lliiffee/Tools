package com.fung.collection;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

public class IteratorSample {
	
	public static void main(String[] args)
	{
		
	}
	
	public void mapIterator()
	{
		Map<String,String> map = new HashMap<String,String>();
		
		
		for(Object o:map.keySet()){
		System.out.println(o); // Map的键
		System.out.println(map.get(o)); // Map的值
		}
		
		for (Entry<String, String> entry: map.entrySet()) {  
			String  key = entry.getKey();  
			String  value = entry.getValue();  
		} 
		
		for (String value : map.values()) {  
		}
		
	}
/*
 *  java.util.Date current=new java.util.Date();
           java.text.SimpleDateFormat sdf=new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); 
           String c=sdf.format(current);
           System.out.println(c);
 */
}
