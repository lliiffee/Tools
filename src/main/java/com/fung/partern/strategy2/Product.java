package com.fung.partern.strategy2;

public abstract class Product {  
	  
    protected ProductTaxBehavior calculateTaxBehavior;  
  
    private String name;  
    private String description;  
    private float weight;  
    private float basePrice;  
  
    public void addProduct(Product product) {  
        throw new UnsupportedOperationException();  
    }  
  
    public void removeProduct(Product product) {  
        throw new UnsupportedOperationException();  
    }  
  
    public Product(String name, String description, float weight,  
            float basePrice) {  
        super();  
        if (name == null)  
            throw new NullPointerException("Name must not be null!");  
        if (description == null)  
            throw new NullPointerException("Description must not be null!");  
        if (weight < 0)  
            throw new IllegalArgumentException("Weight must not be negative!");  
        if (basePrice < 0)  
            throw new IllegalArgumentException(  
                    "BasePrice must not be negative!");  
        this.name = name;  
        this.description = description;  
        this.weight = weight;  
        this.basePrice = basePrice;  
    }  
  
    public Product(String name, float weight, float basePrice) {  
        this(name, "", weight, basePrice);  
    }  
  
    public String getName() {  
        return name;  
    }  
  
    public void setName(String name) {  
        if (name == null)  
            throw new NullPointerException("Name must not be null!");  
        this.name = name;  
    }  
  
    public String getDescription() {  
        return description;  
    }  
  
    public void setDescription(String description) {  
        if (description == null)  
            throw new NullPointerException("Description must not be null!");  
        this.description = description;  
    }  
  
    public float getWeight() {  
        return weight;  
    }  
  
    public void setWeight(float weight) {  
        if (weight < 0)  
            throw new IllegalArgumentException("Weight must not be negative!");  
        this.weight = weight;  
    }  
  
    public float getBasePrice() {  
        return basePrice;  
    }  
  
    public void setBasePrice(float basePrice) {  
        if (basePrice < 0)  
            throw new IllegalArgumentException(  
                    "BasePrice must not be negative!");  
        this.basePrice = basePrice;  
    }  
  
    public float performCalculateTax() {  
        return calculateTaxBehavior.calculateTax(this.basePrice);  
    }  
  
    public float calculatePrice(boolean taxFree) {  
        if (taxFree)  
            return getBasePrice();  
        else  
            return getBasePrice() + performCalculateTax();  
    }  
  
    public String toString() {  
        StringBuffer result = new StringBuffer();  
        result.append("Product: ");  
        result.append(name);  
        if (!description.equals("")) {  
            result.append(" [");  
            result.append(description);  
            result.append("]");  
        }  
        result.append(", type = ");  
        result.append(this.getClass().getName());  
        result.append(", base price = ");  
        result.append(basePrice);  
        result.append(", weight = ");  
        result.append(weight);  
        result.append(".");  
  
        return result.toString();  
    }  
} 