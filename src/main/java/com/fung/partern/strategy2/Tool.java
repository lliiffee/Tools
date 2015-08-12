package com.fung.partern.strategy2;

public class Tool extends Product {  
	  
    public Tool(String name, String description, float weight, float basePrice) {  
        super(name, description, weight, basePrice);  
        calculateTaxBehavior = new SharedTaxCalculator();  
    }  
  
    public Tool(String name, float weight, float basePrice) {  
        this(name, "", weight, basePrice);  
    }  
}  
