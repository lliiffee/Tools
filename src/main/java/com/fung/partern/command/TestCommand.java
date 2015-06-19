package com.fung.partern.command;

public class TestCommand {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		 //Invoker是调用者（司令员），Receiver是被调用者（士兵），MyCommand是命令，实现了Command接口，持有接收对象，看实现代码：
		
		Receiver rec=new Receiver();
		Command command=new MyCommand(rec);
		Invoker iv=new Invoker(command);
		iv.invoke();
		

	}

}
