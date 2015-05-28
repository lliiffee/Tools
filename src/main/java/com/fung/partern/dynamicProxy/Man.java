package com.fung.partern.dynamicProxy;
//被代理对象  
public class Man implements IBlind  
{  
  
    public String name;  
      
    public Man(String name)  
    {  
        this.name=name;  
    }  
    
    public void blinding(){  
        System.out.println(name+" 正在相亲中。。。。。");  
    }  
      
}  
