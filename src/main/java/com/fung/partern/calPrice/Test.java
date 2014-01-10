package com.fung.partern.calPrice;

public class Test {
	
	
	public static void main(String[] args)  
    {  
		
		
		//维护抽象策略类  
        Context context;  
        //采用平价打折，单价为10元，数量5本  
     //   context=new Context(new FlatRateStrategy(10, 5));   
        //采用百分比打折10%  
      //  context=new Context(new PercentageStrategy(10, 5,0.1));  
         
        //利用反射动态决定使用哪一种打折策略  
        DiscountStrategy discountStrategy=(DiscountStrategy)Reflection.getInstance().getStrategyObject(DiscountStrategy.class);  
        //客户端只需要识别策略抽象类即可，与具体的算法解耦  
        context=new Context(discountStrategy);  
    }  

}
