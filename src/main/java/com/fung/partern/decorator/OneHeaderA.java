package com.fung.partern.decorator;

public class OneHeaderA extends OneTicketDecorator{
	
	public OneHeaderA(OneComponent comp){
		super (comp);
		}

	@Override
	public void prtTicket() {
		System.out.println("HeaderA" );
		super.callTrailer();
		
	}

	 

}
