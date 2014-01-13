package com.fung.partern.decorator;

public class OneApp {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		OneComponent c = new OneHeaderA( new OneFooterA( new OneSaleTicket()));
		c.prtTicket();

	}

}
