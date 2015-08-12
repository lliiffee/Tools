package com.fung.partern.strategy2;

public class OrderLine {  
    private Product product;  
    private int amount;  
  
    public OrderLine(Product product, int amount) {  
        if (product == null)  
            throw new NullPointerException("Product must not be null!");  
        if (amount < 0)  
            throw new IllegalArgumentException("Amount must not be negative!");  
        this.product = product;  
        this.amount = amount;  
    }  
  
    public float getPrice(boolean taxFree) {  
        return product.calculatePrice(taxFree) * amount;  
    }  
  
    public float getBasePrice() {  
        return product.getBasePrice() * amount;  
    }  
  
    public float getTax() {  
        return product.performCalculateTax() * amount;  
    }  
  
    public float getWeight() {  
        return product.getWeight() * amount;  
    }  
  
    public Product getProduct() {  
        return product;  
    }  
  
    public int getAmount() {  
        return amount;  
    }  
  
    public void setAmount(int amount) {  
        if (amount < 0)  
            throw new IllegalArgumentException("Amount must not be negative!");  
        this.amount = amount;  
    }  
  
    public String toString() {  
        StringBuffer result = new StringBuffer();  
        result.append("OrderLine: ");  
        result.append("product = ");  
        result.append(product.getName());  
        result.append(", amount = ");  
        result.append(amount);  
        result.append(".");  
        return result.toString();  
    }  
  
}  