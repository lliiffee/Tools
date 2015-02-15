package com.fung.jdbc.common;

import java.lang.reflect.Field;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * 说明：实现结果集到pojo对象的映射
 * @author fung
 * @version 1.0 
 */
public class RowMapper<T>{
	private Class<T> objectClass;
	
	public RowMapper(Class<T> objectClass) {
		this.objectClass = objectClass;
	}
	
	/**
	 * 实现单条记录到对象的映射
	 * @param rs 结果集
	 * @param rowNum 当前行数
	 * @return
	 * @throws SQLException
	 */
	public T mapRow(ResultSet rs, int rowNum) throws SQLException {
		try {
			T object = objectClass.newInstance(); 
			// 得到结果集的字段集合
			ResultSetMetaData metaData = rs.getMetaData();
			int columnNum = metaData.getColumnCount();
			Field[] fields = object.getClass().getDeclaredFields();
			// 设置对象属性的值，若不存在，则设置为null.
			for (int i = 0; i < fields.length; i++) {
				Field field = fields[i];
				int flag = 0;
				for (int j = 1; j <= columnNum; j++) {
					if (metaData.getColumnName(j).toLowerCase().equals(field.getName().toLowerCase())) {
						flag = 1;
						break;
					}
				}
				field.setAccessible(true);
				if (flag == 1) {
					this.typeMapper(field, object, rs);
				}else {
					field.set(object, null);
				}	
				field.setAccessible(false);
			}
			return object;
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 实现多条记录到结果集的映射
	 * @param rs
	 * @return
	 */
	public List<T> mapRows(ResultSet rs){
		int rowNum = 0;
		List<T> objList = new ArrayList<T>();
		try {
			while(rs.next()){
				objList.add(this.mapRow(rs, rowNum++));
			}
			rs.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return objList;
	}
	
	
	/**
	 * 类型的映射
	 * @param field
	 * @param obj
	 * @param rs
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 * @throws SQLException
	 */
	private void typeMapper(Field field, Object obj, ResultSet rs) {
		String typeName = field.getType().getName(); // 得到字段类型
		try {
			if (typeName.equals("java.lang.String")) {
				field.set(obj, rs.getString(field.getName()));
			} else if (typeName.equals("int")
					|| typeName.equals("java.lang.Integer")) {
				field.set(obj, rs.getInt(field.getName()));
			} else if (typeName.equals("long")
					|| typeName.equals("java.lang.Long")) {
				field.set(obj, rs.getLong(field.getName()));
			} else if (typeName.equals("float")
					|| typeName.equals("java.lang.Float")) {
				field.set(obj, rs.getFloat(field.getName()));
			} else if (typeName.equals("double")
					|| typeName.equals("java.lang.Double")) {
				field.set(obj, rs.getDouble(field.getName()));
			} else if (typeName.equals("boolean")
					|| typeName.equals("java.lang.Boolean")) {
				field.set(obj, rs.getBoolean(field.getName()));
			} else if (typeName.equals("java.util.Date")) {
				field.set(obj, rs.getTimestamp(field.getName()));
			} else {
			}
		} catch (IllegalArgumentException e) {			
			e.printStackTrace();
		} catch (IllegalAccessException e) {			
			e.printStackTrace();
		} catch (SQLException e) {			
			e.printStackTrace();
		}
	}
}
