package com.fungspring.beans;

import java.lang.reflect.Method;

/** 
 * 工具类：处理配置文件中K-V对，即创建或注入bean 
 */ 
public class BeanUtil { 
 
    /** 
     * 利用反射进行依赖注入 
     * @param bean 需要注入外部依赖的主体类实例 
     * @param fieldName 需要注入的字段名 
     * @param fieldRef 被注入的组件实例 
     * @throws Exception 
     */ 
    public static void setProperty(Object bean, String fieldName, 
            Object fieldRef) throws Exception { 
 
        // 获取主体类的完整名称 
        String className = getClassName(bean); 
 
        // 获取主体类的所有 Method 
        Class beanClass = Class.forName(className); 
        Method[] methods = beanClass.getMethods(); 
 
        // 准备对应 setter()方法的完整名称 
        String setterName = "set" + fieldName.substring(0, 1).toUpperCase() 
                + fieldName.substring(1, fieldName.length()); 
 
        // 遍历找到对应 setter 方法，并调用 invoke()方法进行注入 
        for (Method m : methods) { 
            if (m.getName().equals(setterName)) { 
                m.invoke(bean, fieldRef); 
                System.out.println("已调用 " + m.getName() + "() 向 " + className 
                        + " 注入 " + getClassName(fieldRef)); 
                return; 
            } 
        } 
        System.out.println(">>注入失败: " + className + "类中不存在" + fieldName 
                + "字段对应的setter()方法 ..."); 
    } 
 
    /** 
     * 根据 Object 实例获取类的完整名称 
     */ 
    private static String getClassName(Object o) { 
        if (o == null) { 
            System.out.println("传入的 Object 实例为 null ..."); 
            return null; 
        } 
        String fullName = o.toString(); 
        String className = fullName.substring(0, fullName.indexOf("@")); 
        return className; 
    } 
} 