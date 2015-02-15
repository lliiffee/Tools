package com.fung.jdbc.common;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fung.date.DateUtil;



/**
 * 说明：自动生成对象的增删改查SQL语句的通用方法工具类
 * @author fung
 * @version 1.0 
 */
public class SQLUtil {	
	/**
	 * 自动生成插入指定对象的SQL语句,不包括对象属性的值为空的字段
	 * @param obj 待生成插入SQL语句的对象
	 * @param tableName 待插入语句对应的数据库表的名称
	 * @return 返回一个包含SQL语句、SQL语句参数值及参数值类型的Map对象
	 */
	public static Map<String, Object> generateInsertExceptNull(Object obj, String tableName) {
		StringBuffer columnStrBuf = new StringBuffer(); // 记录数据表的字段名称
		StringBuffer paramStrBuf = new StringBuffer(); // 记录SQL语句对应插入的占位符
		List<Object> paramValues = new ArrayList<Object>(); // 记录对象参数值
		List<Integer> paramsType = new ArrayList<Integer>(); // 记录参数值类型
		// 查询待插入对象的属性值不为空的属性名称
		List<Object> fieldList = ReflectionUtil.getNotNullField(obj);
		try {
			for (int i = 0; i < fieldList.size(); i++) {
				Field field = (Field) fieldList.get(i);
				field.setAccessible(true);
				// 记录对象属性名称
				columnStrBuf.append(field.getName());
				if (i != fieldList.size() - 1) {
					columnStrBuf.append(",");
				}
				
				// 记录插入SQL语句的参数占位符
				if("class java.util.Date".equals(field.getType().toString())
						&& field.get(obj) != null){
					String timeStr = DateUtil.formatDate((Date)field.get(obj), "yyyy-MM-dd HH:mm:ss");
					paramStrBuf.append("to_date(?, 'yyyy-MM-dd HH24:mi:ss')");
					paramValues.add(timeStr);
					// 记录对象属性的数据类型
					paramsType.add(getOrclDataType(field.getType().toString()));
				}else{
					paramStrBuf.append("?");
					paramValues.add(field.get(obj));
					// 记录对象属性的数据类型
					paramsType.add(getOrclDataType(field.getType().toString()));
				}
				
				if (i != fieldList.size() - 1) {
					paramStrBuf.append(",");
				}
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		// 生成插入操作的SQL语句
		StringBuffer sb = new StringBuffer();
		sb.append("insert into ");
		sb.append(tableName);
		sb.append(" (");
		sb.append(columnStrBuf);
		sb.append(") ");
		sb.append("values");
		sb.append(" (");
		sb.append(paramStrBuf);
		sb.append(")");
		// 将生成的SQL语句、SQL语句参数值及各参数值的数据类型用map保存并返回
		Map<String, Object> sqlMap = new HashMap<String, Object>();
		sqlMap.put("sql", sb.toString());
		sqlMap.put("paramsValues", paramValues.toArray());
		sqlMap.put("paramsTypes", paramsType.toArray());
		return sqlMap;
	}

	/**
	 * 自动生成插入指定对象的SQL语句,包括对象属性的值为空的字段，不包括自增长主键,若不存在，调用时直接置为null.
	 * @param obj 待生成插入SQL语句的对象
	 * @param tableName 待插入语句对应的数据库表的名称
	 * @param keyColumn 数据表主键名称
	 * @return 返回一个包含SQL语句、SQL语句参数值及参数值类型的Map对象
	 * @throws IllegalAccessException 
	 * @throws IllegalArgumentException 
	 */
	public static Map<String, Object> generateInsertWithNull(Object obj, 
			String tableName, String keyColumn) throws IllegalArgumentException, IllegalAccessException {
		StringBuffer columnStrBuf = new StringBuffer(); 
		StringBuffer paramStrBuf = new StringBuffer(); // 记录SQL语句对应插入的占位符
		List<Object> columnNameList = new ArrayList<Object>(); // 记录数据表的字段名称
		List<Object> paramValues = new ArrayList<Object>(); // 记录对象参数值
		List<Integer> paramsType = new ArrayList<Integer>(); // 记录参数值类型
		Field[] fields = obj.getClass().getDeclaredFields();
		for(int i = 0; i < fields.length; i++){
			fields[i].setAccessible(true);
			// 记录对象属性名称
			if(!fields[i].getName().equalsIgnoreCase(keyColumn)){ // 非主键列记录插入SQL语句的参数占位符
				columnStrBuf.append(fields[i].getName());
				columnNameList.add(fields[i].getName());
				if (i != fields.length - 1) {
					columnStrBuf.append(",");
				}
				if("class java.util.Date".equals(fields[i].getType().toString())
						&& fields[i].get(obj) != null){
					String timeStr = DateUtil.formatDate((Date)fields[i].get(obj), "yyyy-MM-dd HH:mm:ss");
					paramStrBuf.append("to_date(?, 'yyyy-MM-dd HH24:mi:ss')");
					paramValues.add(timeStr);
					// 记录对象属性的数据类型
					paramsType.add(getOrclDataType(fields[i].getType().toString()));
				}else{
					paramStrBuf.append("?");
					paramValues.add(fields[i].get(obj));
					// 记录对象属性的数据类型
					paramsType.add(getOrclDataType(fields[i].getType().toString()));
				}
				if (i != fields.length - 1) {
					paramStrBuf.append(",");
				}
			}
		}
		// 生成插入操作的SQL语句
		StringBuffer sb = new StringBuffer();
		sb.append("insert into ");
		sb.append(tableName);
		sb.append(" (");
		sb.append(columnStrBuf);
		sb.append(") ");
		sb.append("values");
		sb.append(" (");
		sb.append(paramStrBuf);
		sb.append(")");
		// 将生成的SQL语句、SQL语句的列名称用map保存并返回
		Map<String, Object> sqlMap = new HashMap<String, Object>();
		
	/*		
		System.out.println(sb.toString());
		System.out.println(columnNameList.toString());
		System.out.println(paramValues.toString());
		System.out.println(paramsType.toString());
	*/		
		sqlMap.put("sql", sb.toString());
		sqlMap.put("columnNameList", columnNameList.toArray());
		sqlMap.put("paramsValues", paramValues.toArray());
		sqlMap.put("paramsTypes", paramsType.toArray());
		return sqlMap;
	}
	
	
	/**
	 * 自动生成更新指定对象的SQL语句
	 * @param obj 待生成更新SQL语句的对象
	 * @param tableName 待更新语句对应的数据库表的名称
	 * @param keyColumn 待更新记录的限定字段
	 * @return 返回一个包含SQL语句及参数值的数组
	 */
	public static Object[] generateUpdate(Object obj, String tableName, String keyColumn) {
		StringBuffer columnSB = new StringBuffer();
		List<Object> params = new ArrayList<Object>();
		Object keyValue = null;
		// 获取属性值不为空的数据表字段名称
		List<Object> fieldList = ReflectionUtil.getNotNullField(obj);
		try {
			for (int i = 0; i < fieldList.size(); i++) {
				Field field = (Field) fieldList.get(i);
				field.setAccessible(true);

				if (field.getName().equalsIgnoreCase(keyColumn)) {
					keyValue = field.get(obj);
				} else {
					columnSB.append(field.getName());	
					if("class java.util.Date".equals(field.getType().toString())
							&& field.get(obj) != null){
						String timeStr = DateUtil.formatDate((Date)field.get(obj), "yyyy-MM-dd HH:mm:ss");
						columnSB.append("=to_date(?, 'yyyy-MM-dd HH24:mi:ss')");
						params.add(timeStr);
					}else{
						columnSB.append("=?");
						params.add(field.get(obj));
					}
					if (i != fieldList.size() - 1) {
						columnSB.append(",");
					}
				}
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		if (keyValue == null) {
			throw new IllegalArgumentException("数据表 [" + tableName+ "] 中的字段'"+keyColumn+"'的值不能为空.");
		}else{
			params.add(keyValue);
		}
		StringBuffer sb = new StringBuffer();
		sb.append("update ");
		sb.append(tableName);
		sb.append(" set ");
		if(columnSB.length() >= 0){
			sb.append(columnSB);
		}else{
			sb.append(keyColumn);
			sb.append("=? ");
			params.add(keyValue);
		}		
		sb.append(" where ");
		sb.append(keyColumn);
		sb.append("=? ");
		return new Object[] { sb.toString(), params.toArray() };
	}
	
	/**
	 * 返回java数据类型对应的Oracle数据库的数据类型值
	 * @param javaType java数据类型
	 * @return 返回Oracle数据表的字段数据类型
	 */
	public static int getOrclDataType(String javaType){
		if("class java.lang.String".equals(javaType)){
			return java.sql.Types.VARCHAR;
		}else if("class java.lang.Integer".equals(javaType) || "int".equals(javaType)){
			return java.sql.Types.INTEGER;
		}else if("class java.lang.Double".equals(javaType) || "double".equals(javaType)){
			return java.sql.Types.DOUBLE;
		}else if("class java.lang.Float".equals(javaType) || "float".equals(javaType)){
			return java.sql.Types.FLOAT;
		}else if("char".equals(javaType)){
			return java.sql.Types.CHAR;
		}else if("class java.lang.Long".equals(javaType) || "long".equals(javaType)){
			return java.sql.Types.NUMERIC;
		}else if("class java.util.Date".equals(javaType)){
			return java.sql.Types.DATE;
		}else{
			return java.sql.Types.VARCHAR;
		}
	}

	/**
	 * 生成SQL语句中的where子句及where子句中参数值
	 * @param obj where条件子句的对象
	 * @return 返回条件不为空的where子句
	 * @throws Exception 
	 * @throws  
	 */
	public static Map<String, Object> generateWhereStr(Object obj) throws  Exception{
		StringBuffer whereStrBuf = new StringBuffer(); // where子句
		List<Object> whereParamValues = new ArrayList<Object>(); // where子句中的参数值
		whereStrBuf.append("  where 1 = 1 ");
		if(obj != null){
			Field[] fields = obj.getClass().getDeclaredFields();
			for(int i = 0; i < fields.length; i++){
				fields[i].setAccessible(true);
				Object columnName = fields[i].get(obj);
				if(columnName != null && !"".equals(columnName)){
					whereStrBuf.append(" and ");
					whereStrBuf.append(fields[i].getName());
					whereStrBuf.append("=?");
					whereParamValues.add(columnName);
				}
			}			
		}
		Map<String, Object> whereMap = new HashMap<String, Object>();
		
/*		System.out.println(whereStrBuf.toString());
		System.out.println(whereParamValues);
		*/
		whereMap.put("whereStr", whereStrBuf.toString());
		whereMap.put("whereParamValues", whereParamValues.toArray());
		return whereMap;
	}

}
