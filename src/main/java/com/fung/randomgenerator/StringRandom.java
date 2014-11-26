package com.fung.randomgenerator;

import java.util.Random;
import java.util.UUID;

public class StringRandom {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

		UUID uuid  =  UUID.randomUUID(); 
		String s = UUID.randomUUID().toString();//用来生成数据库的主键id非常不错。。 
		System.out.println(s);
		System.out.println(getRandomString(12));
		
	}
	
	
	public static String getRandomString(int length) { //length表示生成字符串的长度
	    String base = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghjkmnpqrstuvwxyz23456789";   
	    Random random = new Random();   
	    StringBuffer sb = new StringBuffer();   
	    for (int i = 0; i < length; i++) {   
	        int number = random.nextInt(base.length());  
	        if(sb.length() > 0 && (sb.length()+1)%5==0)
	        {
	        	sb.append("-").append(base.charAt(number));
	        }else
	        {
	        	sb.append(base.charAt(number));
	        }
	           
	    }   
	    return sb.toString();   
	 }   

	

}
