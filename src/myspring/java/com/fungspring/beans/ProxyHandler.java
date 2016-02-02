package com.fungspring.beans;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/** 
 * 代理提供者：实现invoke方法，构造具有横切关注点的动态代理对象 
 */ 
class ProxyHandler implements InvocationHandler { 
    private Object target; // 被代理的目标对象 
    private Advice beforeAdvice;// before 横切关注点 
    private Advice afterAdvice; // after 横切关注点 
 
    public ProxyHandler() { 
        // 空构造方法 
    } 
 
    /** 
     * 3个setter方式的用于依赖注入 
     */ 
    public void setTarget(Object target) { 
        this.target = target; 
    } 
 
    public void setBeforeAdvice(Advice beforeAdvice) { 
        this.beforeAdvice = beforeAdvice; 
    } 
 
    public void setAfterAdvice(Advice afterAdvice) { 
        this.afterAdvice = afterAdvice; 
    } 
 
    /** 
     * 实现invoke()方法，添加before、after关注点以实现AOP 
     */ 
    public Object invoke(Object proxy, Method method, Object[] args) 
            throws Throwable { 
 
        // 在被代理对象业务方法前后添加横切关注点方法 
        this.aspect(this.beforeAdvice, "before"); 
        Object result = method.invoke(this.target, args); 
        this.aspect(this.afterAdvice, "after"); 
 
        return result; 
    } 
 
    /** 
     * 依据是否注入横切关注点来决定before、after的调用 
     */ 
    private void aspect(Advice advice, String aspectName) throws Exception { 
        if (advice != null) { 
            Class c = advice.getClass(); 
            Method[] methods = c.getMethods(); 
            for (Method m : methods) { 
                if (aspectName.equals(m.getName())) { 
                    // 以null参数调用已实现的before、after方法 
                    methods[0].invoke(advice, null); 
                } 
            } 
        } 
    } 
 
    /** 
     * 静态工厂方法，用于获取已注入before、after的动态代理实例 
     */ 
    public Object getProxy(Object target) { 
        Class targetClass = target.getClass(); 
        /* 
         * loader: 目标类的类加载器 interfaces: 目标类已实现的接口 handler: 
         * 转发方法调用的调用处理类实例，这里是当前Handler 
         */ 
        ClassLoader loader = targetClass.getClassLoader(); 
        Class[] interfaces = targetClass.getInterfaces(); 
 
        // 创建并返回动态代理类实例 
        return Proxy.newProxyInstance(loader, interfaces, this); 
    } 
} 