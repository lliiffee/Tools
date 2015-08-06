package com.fung.partern.memento;

public class TextMemento {
	   
	    private TextState text;  
	  
	    public TextState getText() {  
	        return text;  
	    }  
	  
	    public void setText(TextState text) {  
	        this.text = text;  
	    }  
	  
	    public TextMemento(TextState text) {  
	        this.text = text;  
	    }  
}
