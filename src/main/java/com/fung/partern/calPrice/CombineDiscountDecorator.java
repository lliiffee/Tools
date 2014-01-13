package com.fung.partern.calPrice;

public abstract class CombineDiscountDecorator extends DiscountStrategy {
	DiscountStrategy discountStrategy;

	public DiscountStrategy getDiscountStrategy() {
		return discountStrategy;
	}

	public void setDiscountStrategy(DiscountStrategy discountStrategy) {
		this.discountStrategy = discountStrategy;
	}
	
	
}
