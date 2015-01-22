package com.fung.date;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
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
        
        
        
        int i= compare_date("1995-11-12 15:21", "1999-12-11 09:59");
        System.out.println("i=="+i);
        /*
         * 两个Date类型的变量可以通过compareTo方法来比较。此方法的描述是这样的：如果参数 Date 等于此 Date，则返回值 0；
         * 如果此 Date 在 Date 参数之前，则返回小于 0 的值；如果此 Date 在 Date 参数之后，则返回大于 0 的值。 
         */

        
        try {
			System.out.println("= : "+sdf.parse("2015-01-22 00:00:00").compareTo(sdf.parse("2015-01-22 00:00:00")));
			System.out.println("< : "+sdf.parse("2015-01-22 00:00:00").compareTo(sdf.parse("2015-02-20 00:00:00")));
			System.out.println("> : "+sdf.parse("2015-01-22 00:00:00").compareTo(sdf.parse("2015-01-11 00:00:00")));
			
        
        } catch (ParseException e) {
			e.printStackTrace();
		}
	
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
	
	
	  public static int compare_date(String DATE1, String DATE2) {
	        
	        
	        DateFormat df = new SimpleDateFormat("yyyy-MM-dd hh:mm");
	        try {
	            Date dt1 = df.parse(DATE1);
	            Date dt2 = df.parse(DATE2);
	            if (dt1.getTime() > dt2.getTime()) {
	                System.out.println("dt1 在dt2前");
	                return 1;
	            } else if (dt1.getTime() < dt2.getTime()) {
	                System.out.println("dt1在dt2后");
	                return -1;
	            } else {
	                return 0;
	            }
	        } catch (Exception exception) {
	            exception.printStackTrace();
	        }
	        return 0;
	    }
	  

}
