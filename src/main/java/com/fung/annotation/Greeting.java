package com.fung.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


@Documented

/*
 * SOURCE,// Annotation is discarded by the compiler
 CLASS,// Annotation is stored in the class file, but ignored by the VM
 RUNTIME// Annotation is stored in the class file and read by the VM
 */
@Retention(RetentionPolicy.RUNTIME)

@Target( { ElementType.METHOD, ElementType.CONSTRUCTOR })  //我们只允许Greeting注释标注在普通方法和构造函数上，使用在包申明、类名等时，会提示错误信息。
public @interface Greeting {

	public enum FontColor {RED, GREEN, BLUE};

	 

    String name();

 

    String content();

 

    FontColor fontColor() default FontColor.BLUE;
}
