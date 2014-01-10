package com.fung.partern.calPrice;
//不打折类(NoDiscountStrategy) 

public class NoDiscountStrategy extends DiscountStrategy  
{  
    private double price = 0.0;  
    private int copies = 0;  
  
    public NoDiscountStrategy(double price, int copies)  
    {  
        this.price = price;  
        this.copies = copies;  
    }  
  
    public double calculateDiscount()  
    {  
        return price*copies;  
    }  
}  