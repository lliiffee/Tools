package com.fung.partern.command;

public class Invoker {

	private Command command;
	
	public Invoker(Command command) {
		this.command=command;
	}

	public void invoke() {
		this.command.exe();
	}
	
	

}
