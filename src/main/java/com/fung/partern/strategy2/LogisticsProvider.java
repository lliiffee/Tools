package com.fung.partern.strategy2;

import java.util.HashMap;

public class LogisticsProvider {  
    private float risk;  
    private float kilogram;  
    private float price;  
    private String name;  
    private static HashMap<String, float[]> providers;  
    static {  
        float[] ems = { 0.01f, 10, 25 };  
        float[] ups = { 0.08f, 8, 16 };  
        providers.put("EMS", ems);  
        providers.put("UPS", ups);  
    }  
  
    public LogisticsProvider(String name) {  
        if (!providers.containsKey(name.toUpperCase())) {  
            name = "EMS";  
        }  
        float[] property = providers.get(name.toUpperCase());  
        this.name = name.toUpperCase();  
        this.risk = property[0];  
        this.kilogram = property[1];  
        this.price = property[2];  
        // using database to search is better  
    }  
  
    public LogisticsProvider(String name, float risk, float kilogram,  
            float price) {  
        this.name = name;  
        this.risk = risk;  
        this.kilogram = kilogram;  
        this.price = price;  
    }  
  
    public void shipOrder(Order order) {  
        System.out.println("LogisticsProvider: " + this.name);  
        for (OrderLine orderline : order.getOrderLines()) {  
            System.out.println(orderline.toString());  
        }  
        System.out.print("Weight:" + order.getWeight() + "\tPrice:"  
                + order.getBasePrice() + "\tInsurance:"  
                + this.calculateInsurance(order) + "\tFreight:"  
                + this.calculateInsurance(order) + "\tShipment:"  
                + this.calculateShipment(order));  
    }  
  
    public float calculateShipment(Order order) {  
        return calculateInsurance(order) + calculateFreight(order);  
    }  
  
    public float calculateInsurance(Order order) {  
        return order.getBasePrice() * risk;  
    }  
  
    public float calculateFreight(Order order) {  
        int weigthRoundedUp = Math.round(order.getWeight() + 0.49999f);  
        return (weigthRoundedUp * kilogram) + price;  
    }  
  
    public String toString() {  
        StringBuffer result = new StringBuffer();  
        result.append("LogisticsProvider: ");  
        result.append(name);  
        result.append(", type = ");  
        result.append(this.getClass().getName());  
        result.append(".");  
        return result.toString();  
    }  
} 