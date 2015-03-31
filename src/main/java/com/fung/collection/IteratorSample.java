package com.fung.collection;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
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
	
	/*
	 * Map<String,String[]> map = request.getParameterMap();
		 
		for (Entry<String, String[]> entry: map.entrySet()) {
			
			String k = (String)entry.getKey();
			String[] values = entry.getValue();
			String v="";
			for(String vs:values)
			{
				if(v.equals(""))
				v=vs;
				else
					v=v+","+vs;	
			}
			try {
				sb.append(k + "=" + URLEncoder.encode(v, enc) + "&");
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			//sb.append(k + "=" +v + "&");
		}
	 */
}
