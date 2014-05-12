package com.fung.freemarker;

public class Food {

	private String name;
	private int price;
	
	
	public String getName() {
		return name;
	}

	public int getPrice() {
		return price;
	}

	public Food(String name , int price)
	{
		this.name=name;
		this.price=price;
	}
	
	public Food(){}

	public void setName(String name) {
		this.name = name;
	}

	public void setPrice(int price) {
		this.price = price;
	}

}
