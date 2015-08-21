package com.fung.partern.observer2;

import java.util.Observable;

public class ConcretSubject extends Observable{
	 void count(int number)  
	    {  
	        for(; number >= 0; number--)  
	        {  
	            this.setChanged();  
	            this.notifyObservers(number);  
	        }  
	    }  
}
