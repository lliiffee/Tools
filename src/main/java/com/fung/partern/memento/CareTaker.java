package com.fung.partern.memento;

public class CareTaker {
	private TextMemento mementos;  
	  
    public TextMemento getMemento() {  
        return this.mementos;  
    }  
  
    public void saveMemento(TextMemento memento) {  
        this.mementos = memento;  
    }  
}
