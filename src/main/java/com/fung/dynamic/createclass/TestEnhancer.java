package com.fung.dynamic.createclass;

import java.lang.reflect.Method;

import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

public class TestEnhancer {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		 test();  
	}

	//cglib被hibernate用来动态生成po的字节码。CGLIB的底层是java字节码操作框架ASM。
	 static void test() {  
	        while (true) {  
	            Enhancer e = new Enhancer();  
	            e.setSuperclass(OOMObject.class);//要生成OOMObject类的子类
	            e.setUseCache(false);  
	            e.setCallback(new MethodInterceptor() {//设置callback，对原有对象的调用全部转为调用MethodInterceptor的intercept方法  
	                @Override  
	                public Object intercept(Object obj, Method method,  
	                        Object[] args, MethodProxy proxy) throws Throwable {  
	                    System.out.println("Before invoke ");  
	                    Object result = proxy.invokeSuper(obj, args);  
	                    System.out.println("After invoke");  
	                    return result;  
	                }  
	            });  
	            OOMObject ee = (OOMObject) e.create();  
	            System.out.println(ee.getClass().getName());
	            ee.test();  
	        }  
	    }  
	  
	    static class OOMObject {  
	        public void test() {  
	            System.out.println("invokinginginginging....");  
	        }  
	  
	    }  
	    
}
/*
 * 今天简单看下cglib的用法。cglib的Enhancer说起来神奇，用起来一页纸不到就讲完了。它的原理就是用Enhancer生成一个原有类的子类，并且设置好callback到proxy，
 *  则原有类的每个方法调用都会转为调用实现了MethodInterceptor接口的proxy的intercept() 函数
 *  
 *  这段代码动态生成class加载到内存。并且例子还有一个目的，就是让java虚拟机的permgen溢出：

Caused by: java.lang.OutOfMemoryError: PermGen space

这么做的目的是演示，在java虚拟机中，方法区存放了类的相关信息，比如类名、访问修饰符、常量池、字段描述、方法描述等等。当加载到方法区的class太多的时候就可能会报出permgen溢出的错误
 */


/*
 * 下面的代码，也是让permgen溢出，但是原因不同。

这段代码是jvm的运行时常量池溢出。String类的intern方法的意思是如果池中已经包含了这个string类型的字符串，
则返回池中代表这个字符串的String对象。如果池中不存在，则放入池中。

[java] view plain copy
package yuesef;  
  
import java.util.ArrayList;  
import java.util.List;  
  
public class TT {  
    public static void main(String ss[]) {  
        test();  
    }  
  
    static void test() {  
        int i = 0;  
        List list = new ArrayList();  
        while (true) {  
            String s = i++ + "";  
            list.add(s.intern());  
        }  
    }  
}  
 */
