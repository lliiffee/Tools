package com.fung.annotation.createdb;

import java.util.HashMap;
import java.util.Map;

public class Types {
	// 自己定义的一些基本类型，和数据对应就行了,就是组成字符串
	// 这是临时的办法，需要大家从新设计
	public static final String  VARCHAR = "VARCHAR";
	public static final String INT = "INT";
	public static final String BOOLEAN = "BOOLEAN";

	// 默认长度，和数据库对应
	public static final int STRING_LENGTH =32;
	public static final int INT_LENGTH = 10;
	
	// 将类型 已经默认长度，放入集合
	public static Map<String,Integer> map = new HashMap();
	static{
		map.put(VARCHAR, STRING_LENGTH);
		map.put(INT, INT_LENGTH);
		map.put(BOOLEAN, 0);
	}
	
	public static String getType(String type,int length){
		if(type == null){
			throw new RuntimeException("Not recognized the type  :"+type);
		}
		// 防止boolean 这类型
		if( length > 0){
			return type+"("+length+")";
		}
		return type;
	}
	
	public static String getString(){
		return getStirng(VARCHAR, STRING_LENGTH);
	}
	public static String getString(int length){
		return getStirng(VARCHAR, length);
	}
	
	public static String getInt(){
		return getStirng(INT, INT_LENGTH);
	}
	public static String getInt(int length){
		return getStirng(INT, length);
	}
	public static String getBoolean(){
		return BOOLEAN;
	}
	
	private static String getStirng(String str,int length){
		return str+"("+length+")";
	}
}
