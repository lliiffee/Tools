package com.fung.partern.strategy2;

public class Electronics extends Product {  
	  
    public Electronics(String name, String description, float weight,  
            float basePrice) {  
        super(name, description, weight, basePrice);  
        calculateTaxBehavior = new SharedTaxCalculator();  
    }  
  
    public Electronics(String name, float weight, float basePrice) {  
        this(name, "", weight, basePrice);  
    }  
}  