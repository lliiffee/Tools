package com.bbf.sms.advice;

import java.lang.reflect.Method;

import org.springframework.aop.MethodBeforeAdvice;

public class NoteManagerBeforeAdvice implements MethodBeforeAdvice {  
    public void before(Method method, Object[] args, Object target)throws Throwable {  
        System.out.println("在执行NoteManager的run方法之前，执行该方法！");  
    }  
}