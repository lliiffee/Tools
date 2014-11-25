package com.fung.annotation.createdb;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Column {
	// 名字
	String name() default "";
	// 长度
	int length() default 0;
	// 类型
	String type();
	// 约束，这里可以使用其他注解类型。
	Constraints  constraints() default @Constraints;
}
