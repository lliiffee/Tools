package com.fung.annotation.createdb;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Constraints{
        // 是否主键，是否为空 等
	boolean primaryKey() default false;
	boolean allowNull() default true;
	boolean unique() default false;
}