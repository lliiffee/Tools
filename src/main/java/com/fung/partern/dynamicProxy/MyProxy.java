package com.fung.partern.dynamicProxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;

public class MyProxy {

	public static void main(String[] args) {  
		// 被代理类的实例  
        Man man  = new Man("小明");  
            
     // 用被代理类的实例创建动态代理类的实例，用于真正调用处理程序  
        InvocationHandler handler = new MatchMaker(man);  
         
      
 
        // 获得被代理类的类加载器，使得JVM能够加载并找到被代理类的内部结构，以及已实现的interface  
        ClassLoader loader = man.getClass().getClassLoader();  
 
        // 获得被代理类已实现的所有接口interface,使得动态代理类的实例  
        Class<?>[] interfaces = man.getClass().getInterfaces();  
 

        
     // 获得被代理类的类加载器，使得JVM能够加载并找到被代理类的内部结构，以及已实现的interface
      //  Class<?> classType = handler.getClass();  
       // ClassLoader loader =classType.getClassLoader();
        
        //使用反射生成代理对象        
        //java.lang.reflect.Proxy.newProxyInstance方法根据传入的接口类型 （obj.getClass.getInterfaces()）动态构造一个代理类实例返回，这也说明了为什么动态代理实现要求其所代理的对象一定要实现 一个接口。这个代理类实例在内存中是动态构造的，它实现了传入的接口列表中所包含的所有接口。
        
        IBlind ok = (IBlind)Proxy.newProxyInstance(loader,interfaces,handler);  
           
        ok.blinding();  
  
    }  
}
