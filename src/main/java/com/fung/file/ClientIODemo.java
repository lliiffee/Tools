package com.fung.file;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.nio.charset.Charset;

public class ClientIODemo {

	 public static void main(String[] args) {  
	        try {  
	            Socket socket=new Socket("127.0.0.1",12345);  
	              
	            InputStream in=socket.getInputStream();  
	            OutputStream out=socket.getOutputStream();  
	              
	            //客户端发送消息  
	            String message="[client]: send a message:发送消息！";  
	            byte[] context=message.getBytes();  
	            out.write(context);  
	            out.flush();  
	            System.out.println(message);  
	              
	            //客户端接受消息  
	                        StringBuilder feedback = new StringBuilder();  
	            byte[] buffer=new byte[1000];  
	            in.read(buffer);  
	            feedback.append(new String(buffer,Charset.defaultCharset()));  
	              
	            System.out.println("[client]: receive a message:"+feedback.toString());  
	              
	            in.close();  
	            out.close();  
	              
	            socket.close();  
	        } catch (UnknownHostException e) {  
	            e.printStackTrace();  
	        } catch (IOException e) {  
	            e.printStackTrace();  
	        }  
	          
	    }  

}
