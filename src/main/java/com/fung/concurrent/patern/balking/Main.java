package com.fung.concurrent.patern.balking;

public class Main {

	public static void main(String[] args){
		Data data=new Data("data.txt","(empty)");
		new ChangeThread("changeThread",data).start();
		new SaverThread("saverThread",data).start();
	}
}
