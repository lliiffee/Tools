package com.fung.partern.observer2;

import java.util.Observer;

public class TwoObservers {
	 public static void main(String[] args)  
	    {  
	        ConcretSubject watched = new ConcretSubject();  
	          
	        Observer watcher1 = new concretObsver1();  
	        Observer watcher2 = new concretObsver2();  
	          
	        watched.addObserver(watcher1);  
	        watched.addObserver(watcher2);  
	        
	        watched.count(10);  
	    } 
}
