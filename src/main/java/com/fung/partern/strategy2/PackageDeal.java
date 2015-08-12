package com.fung.partern.strategy2;

import java.util.ArrayList;

public class PackageDeal extends Product {  
    ArrayList<Product> packagedeal = new ArrayList<Product>();  
  
    private float discount;  
  
    public float getDiscount() {  
        return discount;  
    }  
  
    public void setDiscount(float discount) {  
        this.discount = discount;  
    }  
  
    public PackageDeal(String name, float discount) {  
        super(name, "PackageDeal", 0, 0);  
        this.discount = discount;  
    }  
  
    public void addProduct(Product product) {  
        packagedeal.add(product);  
    }  
  
    public void removeProduct(Product product) {  
        packagedeal.remove(product);  
    }  
  
    @Override  
    public float performCalculateTax() {  
        float tax = 0;  
        for (Product product : packagedeal) {  
            tax += product.performCalculateTax();  
        }  
        return tax * discount;  
    }  
  
    @Override  
    public float getWeight() {  
        float weight = 0;  
        for (Product product : packagedeal) {  
            weight += product.getWeight();  
        }  
        this.setWeight(weight);  
        return weight;  
    }  
  
    @Override  
    public float getBasePrice() {  
        float price = 0;  
        for (Product product : packagedeal) {  
            price += product.getBasePrice();  
        }  
        this.setBasePrice(price * discount);  
        return price * discount;  
    }  
  
    @Override  
    public String toString() {  
        StringBuffer result = new StringBuffer();  
        result.append("PackageDeal starts >>");  
        for (Product product : packagedeal) {  
            result.append(product.toString());  
        }  
        result.append("PackageDeal ends >>");  
        return result.toString();  
    }  
  
}  
