package com.bbf.sms;

import java.lang.reflect.Method;

import org.springframework.aop.AfterReturningAdvice;

public class NoteManagerAfterReturningAdvice implements AfterReturningAdvice {  
    public void afterReturning(Object returnValue, Method method, Object[] args, Object target) throws Throwable {  
        System.out.println("在执行NoteManager的run方法之后，执行该方法！");  
    }  
}  
