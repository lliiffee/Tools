package com.fung.partern.strategy2;

public class Software extends Product {  
	  
    public Software(String name, String description, float weight,  
            float basePrice) {  
        super(name, description, weight, basePrice);  
        calculateTaxBehavior = new SoftwareTaxCalculator();  
    }  
  
    public Software(String name, float weight, float basePrice) {  
        this(name, "", weight, basePrice);  
    }  
} 
