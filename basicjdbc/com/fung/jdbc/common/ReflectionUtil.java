package com.fung.jdbc.common;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * 说明：扩展JAVA对象的反射机制
 * @author fung
 * @version 1.0 
 */
public class ReflectionUtil {

	/**
	 * 设置对象的指定属性名称的属性值
	 * @param obj 待设置的对象
	 * @param fieldName 对象属性名称
	 * @param value 属性值
	 */
	public static void setFieldValue(Object obj, String fieldName, Object value) {
		Class<? extends Object> c = obj.getClass();
		try {
			Field field = null;
			Field[] fields = c.getDeclaredFields();
			for(int i = 0; i < fields.length; i++){
				String fieldNameTemp = fields[i].getName();
				if(fieldNameTemp.equalsIgnoreCase(fieldName)){
					field = c.getDeclaredField(fieldNameTemp);
					field.setAccessible(true);
					field.set(obj, value);
					return;
				}
			}	
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * 获取对象的指定属性名称的属性值
	 * @param obj 待设置的对象
	 * @param fieldName 对象属性名称
	 */
	public static Object getFieldValue(Object obj, String fieldName) {
		Class<? extends Object> c = obj.getClass();
		Field[] fields = c.getDeclaredFields();
		try {
			for(int i = 0; i < fields.length; i++){
				if(fields[i].getName().equalsIgnoreCase(fieldName)){
					fields[i].setAccessible(true);
					return fields[i].get(obj);
				}
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		return null;
	}

	/**
	 * 获取对象的属性值不为空的属性名称
	 * @param obj 待获取的对象
	 * @return 返回属性值不为空的对象的属性名称列表
	 */
	public static List<Object> getNotNullField(Object obj) {
		Class<? extends Object> c = obj.getClass();
		List<Object> list = new ArrayList<Object>();
		try {
			Field[] fields = c.getDeclaredFields();
			for(int i = 0; i < fields.length; i++){
				fields[i].setAccessible(true);
				if(fields[i].get(obj) != null){
					list.add(fields[i]);
				}
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		return list;
	}	 
}