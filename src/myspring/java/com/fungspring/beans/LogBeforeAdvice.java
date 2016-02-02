package com.fungspring.beans;

class LogBeforeAdvice implements BeforeAdvice { 
	 
    public void before() { 
        System.out.println("原业务方法被调用【之前】先打印日志..."); 
    } 
} 