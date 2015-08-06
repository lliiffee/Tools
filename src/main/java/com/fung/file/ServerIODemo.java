package com.fung.file;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.Charset;

public class ServerIODemo {

	public static void main(String[] args) {  
        try {  
            ServerSocket server=new ServerSocket(12345);  
            while(true){  
                Socket socket = server.accept();//阻塞  
                  
                InputStream in=socket.getInputStream();  
                OutputStream out=socket.getOutputStream();  
  
                StringBuilder message=new StringBuilder();  
                byte[] buffer=new byte[10];  
                int i=0;  
                while(true){  
                    i++;  
                    in.read(buffer); //当发现没有值read的时候，就阻塞住了  
                    message.append(new String(buffer,Charset.defaultCharset()));  
                    if(i>10){  
                        break;  
                    }  
                }  
                System.out.println("[server]:"+message);  
                  
                String messages="[server]: send a message:发送消息！";  
                byte[] context=messages.getBytes();  
                out.write(context);  
                out.flush();  
  
                in.close();  
                out.close();  
                socket.close();  
            }  
              
        } catch (IOException e) {  
            e.printStackTrace();  
        }  
    }  

}
