package com.fung.partern.command;

public class MyCommand implements Command {

	private Receiver rec;
	
	public MyCommand(Receiver rec) {
		this.rec=rec;
	}

	public void exe() {
		 rec.action();
	}

}
