package com.fung.reflection;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class ReflectClass {

	/**
	 * @param args
	 */
	public static void main(String[] args) throws Exception{
		Person p = new Person();
		p.setId(0);
		p.setName("张三");
		reflect(p);
		
		String m="getName";  
		 Method method = p.getClass().getMethod(m);     
		System.out.println( method.invoke(p));  
	}
	
	private Object invokeNoArgsMethod(Object o,String fieldName)
	{
		
			
		
		if (null == fieldName || "".equals(fieldName)) { 
	        return null; 
	    } else
	    {
	    	try
			{
	    	 Method method = o.getClass().getMethod("get"+fieldName.substring(0, 1).toUpperCase()+fieldName.substring(1));     
			  return method.invoke(o); 
			}catch(Exception e)
			{
				e.printStackTrace();
				return null;
			}
	    }
	}
	public Object invokeMethod(String className, String methodName,
			Object[] args) throws Exception{

		Class ownerClass = Class.forName(className);	
		Object owner = ownerClass.newInstance();
		   
        Class[] argsClass = new Class[args.length];    
   
        for (int i = 0, j = args.length; i < j; i++) {    
        	
           argsClass[i] = args[i].getClass();      	       
        }    
   
        Method method = ownerClass.getMethod(methodName, argsClass);   
		return method.invoke(owner, args);
	}
	
	public static void reflect(Object obj) {
		if (obj == null) return;
		Field[] fields = obj.getClass().getDeclaredFields();
		for (int j = 0; j < fields.length; j++) {
			fields[j].setAccessible(true);
			// 字段名
			System.out.print(fields[j].getName() + ",");
			// 字段值
			if (fields[j].getType().getName().equals(
					java.lang.String.class.getName())) {
				// String type
				try {
					System.out.print(fields[j].get(obj));
				} catch (IllegalArgumentException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} else if (fields[j].getType().getName().equals(
					java.lang.Integer.class.getName())
					|| fields[j].getType().getName().equals("int")) {
				// Integer type
				try {
					System.out.println(fields[j].getInt(obj));
				} catch (IllegalArgumentException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			// 其他类型。。。
		}
		System.out.println();
	}
}

class Person {
	private int id;
	private String name;

	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
