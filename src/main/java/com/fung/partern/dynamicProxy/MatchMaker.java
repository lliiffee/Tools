package com.fung.partern.dynamicProxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

public class MatchMaker  implements InvocationHandler{  
  
    private Object object;//被代理的对象不确定  
      
    public MatchMaker(Object object)  
    {  
        this.object=object;       
    }  
    //相亲前  
    public void before()  
    {  
        System.out.println("帮被代理对象美言几句。。。。");  
    }     
    //相亲后  
    public void after()  
    {  
        System.out.println("相亲成功。。。。。。。");  
    }  
    //相亲  
      
    /*
     * InvocationHandler.invoke方法将在被代理类的方法被调用之前触发。通过这个方法，我们可以在被代理类方法调用的前后进行一些处 理，如代码中所示，InvocationHandler.invoke方法的参数中传递了当前被调用的方法（Method）,以及被调用方法的参数。同 时，可以通过method.invoke方法调用被代理类的原始方法实现。这样就可以在被代理类的方法调用前后写入任何想要进行的操作。(non-Javadoc)
     * @see java.lang.reflect.InvocationHandler#invoke(java.lang.Object, java.lang.reflect.Method, java.lang.Object[])
     */
    public Object invoke(Object proxy, Method method, Object[] args)  
            throws Throwable {  
        //相亲前  
        before();  
        //相亲中  
        method.invoke(object, args);  
        //相亲后  
        after();  
        return null;  
        
        /*
         * Object result = null;
			　if (!method.getName().startsWith("save")) {
			　　UserTransaction tx = null;
			　　try {
			　　　tx = (UserTransaction) (new InitialContext().lookup("java/tx"));
			　　　result = method.invoke(originalObject, args);
			　　　tx.commit();
			　　} catch (Exception ex) {
			　　　if (null != tx) {
			　　　　try {
			　　　　　tx.rollback();
			　　　　} catch (Exception e) {
			　　　}
			　　}
			　}
			} else {
			　result = method.invoke(originalObject, args);
			}
			return result;
         */
    }  
      
}  
