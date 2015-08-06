package com.fung.file;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

public class FileNIODemo {

	public static void main(String[] args) {  
        //获取文件  
        File fileRead = new File("fileIORead.txt");  
        File fileWrite = new File("fileIOWrite.txt");  
          
          
        try {  
            FileInputStream input = new FileInputStream(fileRead);  
            FileOutputStream out=new FileOutputStream(fileWrite);  
            FileChannel inputChannel= input.getChannel();  
            FileChannel outputChannel=out.getChannel();  
              
            ByteBuffer buffer=ByteBuffer.allocate(10);  
            int i=0;  
            while(true){  
                buffer.clear();  
                int res=inputChannel.read(buffer);  
                if(res==-1){  
                    break;  
                }  
                buffer.flip();  
                outputChannel.write(buffer);  
                System.out.println("i:"+i++);  
            }  
              
            inputChannel.close();  
            input.close();  
            outputChannel.close();  
            out.close();  
        } catch (FileNotFoundException e) {  
            e.printStackTrace();  
        } catch (IOException e) {  
            e.printStackTrace();  
        }  
    }  
}
