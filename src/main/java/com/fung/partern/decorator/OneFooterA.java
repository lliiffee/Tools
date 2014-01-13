package com.fung.partern.decorator;

public class OneFooterA extends OneTicketDecorator{

	public OneFooterA(OneComponent comp) {
		super(comp);
		 
	}

	@Override
	public void prtTicket() {
		System.out.println("FooterA" );
		super.callTrailer();
		
	}

}
