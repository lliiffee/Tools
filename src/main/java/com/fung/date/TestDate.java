package com.fung.date;

import java.util.Random;

public class TestDate {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		java.util.Date current=new java.util.Date();
        java.text.SimpleDateFormat sdf=new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); 
        String c=sdf.format(current);
        System.out.println(c + getRandomString());

	}
	//取某个范围的任意数
	public static String getRandom(int min, int max)
	{
	Random random = new Random();
	int s = random.nextInt(max) % (max - min + 1) + min;
	return String.valueOf(s);
	}
	
	public static String getRandomString() { //length表示生成字符串的长度
	    String base = "abcdefghijklmnopqrstuvwxyz0123456789";   
	    Random random = new Random();   
	    StringBuffer sb = new StringBuffer();   
	    
	        int number = random.nextInt(base.length());   
	        sb.append(base.charAt(number)).append("***");
	        int last=random.nextInt(base.length());
	        sb.append(base.charAt(last));
	    
	    return sb.toString();   
	 }
	

}
