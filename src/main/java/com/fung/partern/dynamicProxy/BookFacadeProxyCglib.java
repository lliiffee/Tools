package com.fung.partern.dynamicProxy;

import java.lang.reflect.Method;

import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

public class BookFacadeProxyCglib implements MethodInterceptor {

	 private Object target;  

	    /** 
	     * 创建代理对象 
	     *  
	     * @param target 
	     * @return 
	     */  
	    public Object getInstance(Object target) {  
	        this.target = target;  
	        Enhancer enhancer = new Enhancer();  
	        enhancer.setSuperclass(this.target.getClass());    ///要生成target 所属类的子类
	       
	        enhancer.setCallback(this);   // 回调方法  this 实现的 MethodInterceptor 回调方法 intercept();
	        
	        // 创建代理对象  
	        return enhancer.create();  
	    } 
	    
	@Override
	public Object intercept(Object obj, Method method, Object[] args,
			MethodProxy proxy) throws Throwable {
		Object result=null;  
        System.out.println("事物开始"+method.getName());  
        //执行方法  
        result=proxy.invoke(target, args);  
        System.out.println("事物结束");  
        return result;   
	}

	

}
