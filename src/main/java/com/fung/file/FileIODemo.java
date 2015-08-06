package com.fung.file;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class FileIODemo {

	// BIO：
	 public static void main(String[] args) {  
	        //获取文件  
	        File fileRead = new File("fileIORead.txt");  
	        File fileWrite = new File("fileIOWrite.txt");  
	          
	        //字节流  
	        try {  
	            FileInputStream input=new FileInputStream(fileRead);  
	            FileOutputStream out=new FileOutputStream(fileWrite);  
	              
	            //缓冲数组  
	            byte[] buffer=new byte[10];  
	            int i=0;  
	            while(input.read(buffer)!=-1){  
	                System.out.println("i:"+i++);  
	                out.write(buffer);  
	            }  
	            input.close();  
	            out.close();  
	              
	        } catch (FileNotFoundException e) {  
	            e.printStackTrace();  
	        } catch (IOException e) {  
	            e.printStackTrace();  
	        }  
	    }  

}
