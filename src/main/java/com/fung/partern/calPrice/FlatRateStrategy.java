package com.fung.partern.calPrice;

/** 
 * 平价销售图书，不进行打折算法类 
 * @author LLS 
 * 
 */  
public class FlatRateStrategy extends DiscountStrategy  
{  
    //保存图书单价、数量、总额  
    private double amount;  
    private double price = 0;  
    private int copies = 0;  
  
    public FlatRateStrategy(double price, int copies)  
    {  
        this.price = price;  
        this.copies = copies;  
    }  
      
    public double getAmount()  
    {  
        return amount;  
    }  
  
    public void setAmount(double amount)  
    {  
        this.amount = amount;  
    }  
    //覆盖了抽象类的方法  
    public double calculateDiscount()  
    {  
        return copies * amount;  
    }  
}  