package com.fung.annotation;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;


public class AnnotationIntro {

	public static void main(String[] args)
	{
			 Method[] methods;
			try {
				methods = Class.forName("com.fung.annotation.AnnotationTest").getDeclaredMethods();
				 Annotation[] annotations;
					
					
					
			     for (Method method : methods) {
			    	 System.out.println(method.getName());
			         annotations = method.getAnnotations();
			         System.out.println(annotations.length);
			         for (Annotation annotation : annotations) {
			
			             System.out.println(method.getName() + " : "
			                     + annotation.annotationType().getName());
			         }
			
				}
			} catch (SecurityException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		
		    
 }

}
