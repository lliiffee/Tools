package com.fung.annotation.createdb;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;


public class CreateTable {
	
	public static void main(String[] args) {
		// 类路径
		System.out.println(getCreateSQL("com.fung.annotation.createdb.TestTable"));
	}
	
	@SuppressWarnings("unused")
	public static String getCreateSQL(String className){
		try {
			// 加载类
			Class<?> c = Class.forName(className);
			// 获得指定类型的注解对象
			Table table = c.getAnnotation(Table.class); 
			if(table == null){
				System.out.println("No Table annotation in class "+ className);
				return null;
			}
			String tableName = table.name();
			if(tableName.length() == 0){
				// 如果没指定长度， 可以默认以类的名字 命名表名
				tableName = c.getName().toUpperCase();
			}
			List<String> columns = new ArrayList<String>();
			
			// 遍历所有字段
			for(Field field : c.getDeclaredFields()){
				String columnName = null;
				String columnType = null;
				// 获得每个字段上的注解信息,这里不需要继承的注解
				// 还有其他方法，具体可以去看API
				Annotation[] anns = field.getDeclaredAnnotations();
				if(anns.length == 0){
					// 如果该字段没有注解，表示这个字段，不需要生成
					continue;
				}else{
					// 获得该字段的注解信息,默认这设置的column注解信息
					Column col = (Column) anns[0];
					// 获得建表 语句  字符串
					String name = col.name();
					String type = col.type();
					Integer length = col.length();
					String constraint = getConstraints(col.constraints());
					if(name.length() == 0){
						// 获得默认字段名
						columnName = field.getName();
					}else{
						columnName = name;
					}
					if(type.length() == 0){
						// 获得默认类型
						columnType = field.getType().toString();
					}else{
						columnType = type;
					}
					if(length == 0){
						// 获取默认长度
						length = Types.map.get(type);
						if(length == null){
							throw new RuntimeException("Type cant't be solved :"+type);
						}
					}
					type = Types.getType(type,length);
					columns.add(columnName + " "+ type+constraint);
				}
			}
			if(columns.size() == 0){
				throw new RuntimeException("There is no field in "+className);
			}
			StringBuilder createCommand = new StringBuilder("CREATE TABLE "+tableName +" (");
			for(String column : columns){
				createCommand.append("\n "+column +" ,");
			}
			String createTable = createCommand.substring(0,createCommand.length()-1)+" \n );";
			return createTable;
			
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * 获得约束条件
	 * @param con
	 * @return
	 */
	private static String getConstraints(Constraints con){
		String constraints = "";
		if(!con.allowNull()){
			constraints += " NOT NULL";
		}
		if(con.primaryKey()){
			constraints += " PRIMARY KEY";
		}
		if(con.unique()){
			constraints += " UNIQUE ";
		}
		return constraints;
	}
	
	/**
	 * 获得所需要的字段
	 * @param fields
	 * @return
	 */
	public static List<Field> getNeedField(Field[] fields){
		List<Field> allFileds = new ArrayList<Field>();
		for(Field field : fields){
			// 获得每个字段上的注解信息,这里不需要继承的注解
			Annotation[] anns = field.getDeclaredAnnotations();
			if(anns.length != 0){
				// 如果该字段没有注解，表示这个字段，不需要生成
				allFileds.add(field);
			}
		}
		return allFileds;
	}
}
