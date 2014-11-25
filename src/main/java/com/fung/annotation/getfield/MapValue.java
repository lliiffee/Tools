package com.fung.annotation.getfield;



import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
/**
 * 放在方法上的注解，这个注解可以映射方法的索引
 * @author 草原战狼
 *
 */
@Target(value = {ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface MapValue {
    /**
     * 标注方法的索引
     * @return 方法索引值
     */
    int index() default 0;
}

 