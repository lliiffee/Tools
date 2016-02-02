package com.fungspring.beans;

class LogAfterAdvice implements AfterAdvice { 
	 
    public void after() { 
        System.out.println("原业务方法被调用【之后】再打印日志..."); 
    } 
} 
