package com.fung.partern.strategy2;

public class SoftwareTaxCalculator implements ProductTaxBehavior {  
    private static final float TAX_RATE_SHARE = 0.06f;  
  
    public float calculateTax(float price) {  
        return price * TAX_RATE_SHARE;  
    }  
  
} 