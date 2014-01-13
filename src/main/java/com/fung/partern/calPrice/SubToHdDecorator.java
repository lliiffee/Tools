package com.fung.partern.calPrice;

public class SubToHdDecorator extends CombineDiscountDecorator{

	public SubToHdDecorator()
	{
		
	}
	
	public SubToHdDecorator(DiscountStrategy discountStrategy)
	{
		this.setDiscountStrategy(discountStrategy);
	}
	@Override
	public double calculateDiscount() {
		// TODO Auto-generated method stub
		return this.getDiscountStrategy().calculateDiscount();
	}

}
