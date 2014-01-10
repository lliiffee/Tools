package com.fung.partern.calPrice;

/** 
 * 维护抽象策略类的引用 
 * 
 */  
public class Context {  
    //维护策略抽象类的一个引用  
    DiscountStrategy discountStrategy;  
    //传入具体策略对象  
    public Context(DiscountStrategy discountStrategy)   
    {  
        this.discountStrategy=discountStrategy;  
    }  
    //根据具体策略对象调用其方法  
    public void ContextInterface()  
    {  
        discountStrategy.calculateDiscount();  
    }  
}  