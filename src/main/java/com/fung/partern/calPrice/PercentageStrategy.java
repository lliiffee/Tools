package com.fung.partern.calPrice;

/** 
 * 折扣销售图书类 
 * 
 */  
public class PercentageStrategy extends DiscountStrategy  
{     
    //保存单价、数量、总额  
    private double percent = 0.0;  
    private double price = 0.0;  
    private int copies = 0;  
  
    public PercentageStrategy()
    {
    	
    }
    public PercentageStrategy(double price, int copies,double percent)  
    {   this.percent=percent;  
        this.price = price;  
        this.copies = copies;  
    }  
  
    public double getPercent()  
    {  
        return percent;  
    }  
  
    public void setPercent(double percent)  
    {  
        this.percent = percent;  
    }  
    //覆盖父类的抽象方法  
    public double calculateDiscount()  
    {  
        return copies * price * percent;  
    }  
  
}  
