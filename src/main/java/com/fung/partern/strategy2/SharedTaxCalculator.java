package com.fung.partern.strategy2;

public class SharedTaxCalculator implements ProductTaxBehavior {  
    private static final float TAX_RATE_ELECTRONICS = 0.21f;  
  
    public float calculateTax(float price) {  
        return price * TAX_RATE_ELECTRONICS;  
    }  
  
} 