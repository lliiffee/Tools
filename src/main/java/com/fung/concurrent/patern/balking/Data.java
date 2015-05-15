package com.fung.concurrent.patern.balking;

import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;

public class Data {

	 private final String fileName;
	 private String content;
	 private boolean changed;
	public Data(String fileName, String content) {
		super();
		this.fileName = fileName;
		this.content = content;

	}
	 
	 public synchronized void change(String newContent){ //sate changing method
		 content=newContent;
		 changed=true;
	 }
	 
	 public synchronized void save()throws IOException{ //gard method
		 if(!changed){
			 System.out.println(Thread.currentThread().getName() +" unchanged cancel..");
			 return;
		 }
		 doSave();
		 changed=false;
	 }
	 
	 private void doSave() throws IOException{
		 System.out.println(Thread.currentThread().getName() +" call doSave, content="+content);
		 Writer writer=new FileWriter(fileName);
		 writer.write(content);
		 writer.close();
	 }

}
