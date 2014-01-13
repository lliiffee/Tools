package com.fung.partern.decorator;

public abstract class OneTicketDecorator extends OneComponent {
	private OneComponent myTrailer;
	public OneTicketDecorator(OneComponent comp){
	myTrailer = comp;
	}
	public void callTrailer(){
	if (myTrailer != null )
	myTrailer.prtTicket();
	}
}
