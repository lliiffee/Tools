package com.fung.annotation;

import java.lang.annotation.Documented;

public class AnnotationTest {

	/**
	 * @param args
	 */
	@NewAnnotation("Just a Test.")
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		sayHello();
	}
	
	@Greeting(name="test", content="test")
	@NewAnnotation(value="Hello NUMEN.")
    public static void sayHello() {

        System.out.println("1");

    }
	

}
