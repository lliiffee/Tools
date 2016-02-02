package com.fung.file;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;

public class Test {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		//String ct=OpFile.read("E:\\temp\\ttt.txt", "UTF-8");
		////System.out.println(ct);
		//String arr[] =ct.split("\"openid\":\"");
		//System.out.println(arr[1].split("\",\"province\"")[0]);
		//System.out.println(arr[1].split("\"unionid\":\"")[1].split("\"")[0]);
		read("E:\\temp\\ttt.txt", "UTF-8");
	}

	 public   static  void read(String fileName, String encoding) { 
		 
		 
	        StringBuffer fileContent  =   new  StringBuffer(); 
	        String line  =   null ; 
	         try  { 
	            FileInputStream fis  =   new  FileInputStream(fileName); 
	            InputStreamReader isr  =   new  InputStreamReader(fis, encoding); 
	            BufferedReader br  =   new  BufferedReader(isr); 
	           
	             while  ((line  =  br.readLine())  !=   null ) { 
	              //  fileContent.append(line); 
	              //  fileContent.append(System.getProperty( " line.separator " )); 
	            	 
	            	 String ct=line;
	         		System.out.println(ct);
	         		String arr[] =ct.split("\"openid\":\"");
	         		System.out.println(arr[1].split("\",\"province\"")[0]);
	         		System.out.println(arr[1].split("\"unionid\":\"")[1].split("\"")[0]);
	            } 
	        }  catch  (Exception e) { 
	            e.printStackTrace(); 
	            System.out.println("#########################"+line);
	        } 
	        
	    } 
	 
}
