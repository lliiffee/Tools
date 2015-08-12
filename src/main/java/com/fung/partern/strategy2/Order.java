package com.fung.partern.strategy2;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Order {  
    private boolean taxFree;  
    private List<OrderLine> orderLines;  
  
    public Order() {  
        this(false); // by default orders are not tax free  
    }  
  
    public Order(boolean taxFree) {  
        this.taxFree = taxFree;  
        orderLines = new ArrayList<OrderLine>();  
    }  
  
    public float getPrice() {  
        float price = 0;  
        for (OrderLine line : orderLines) {  
            price += line.getPrice(taxFree);  
        }  
        return price;  
    }  
  
    public float getBasePrice() {  
        float basePrice = 0;  
        for (OrderLine line : orderLines) {  
            basePrice += line.getBasePrice();  
        }  
        return basePrice;  
    }  
  
    public float getTax() {  
        if (!taxFree) {  
            float tax = 0;  
            for (OrderLine line : orderLines) {  
                tax += line.getTax();  
            }  
            return tax;  
        } else  
            return 0f;  
    }  
  
    public float getWeight() {  
        float weight = 0;  
        for (OrderLine line : orderLines) {  
            weight += line.getWeight();  
        }  
        return weight;  
    }  
  
    public void addOrderLine(OrderLine orderLine) {  
        orderLines.add(orderLine);  
    }  
  
    public void removeOrderLine(OrderLine orderLine) {  
        orderLines.remove(orderLine);  
    }  
  
    public int getOrderLineCount() {  
        return orderLines.size();  
    }  
  
    public OrderLine getOrderLine(int item) {  
        if (item < 0)  
            throw new IndexOutOfBoundsException("Invalid OrderLine number!");  
        if (item >= orderLines.size())  
            throw new IndexOutOfBoundsException(  
                    "There are not that many OrderLines!");  
        return orderLines.get(item);  
    }  
  
    public List<OrderLine> getOrderLines() {  
        return Collections.unmodifiableList(orderLines);  
    }  
  
}  