package com.fung.jdbc.common;

import java.io.Serializable;
import java.sql.ResultSet;
import java.util.List;
import java.util.Map;

 

/**
 * 说明：封装实现数据库的CRUD相关的底层操作。
 * @author fung
 * @version 1.0 
 */
public class BaseDao{
	// 获取数据库链接实例
    private DBConn dbconn = new DBConn();
	/** 数据库表的前缀  */
	public static final String TB_PREFIX = "tb_jkit_";
	/** 数据库表的查询前缀  */
	public static final String SELECT_TB_PREFIX = "select * from tb_jkit_";
	/** 升序排列  */
	public static final String ASC = "asc";
	/** 降序排列 */
	public static final String DESC = "desc";

	/**
	 * 根据ID查找对象 
	 * @param classType 对象类型
	 * @param columnName 编号字段名称
	 * @param id 对象编号
	 * @return 返回实体对象
	 */
	public <T> T queryById(Class<T> classType, String columnName, Serializable id) 
			throws Exception{
		StringBuffer sqlBuffer = new StringBuffer();
		sqlBuffer.append(SELECT_TB_PREFIX);
		sqlBuffer.append(this.toLowerCaseFirstOne(classType.getSimpleName()));
		sqlBuffer.append(" where ");
		sqlBuffer.append(columnName);
		sqlBuffer.append(" = ? ");
		this.showSQL(sqlBuffer.toString());
		return dbconn.execQuery(sqlBuffer.toString(), new RowMapper<T>(classType),
					new Object[]{id}).get(0);
	}
	
	/**
	 * 查询所有指定class类型的对象信息
	 * @param classType 对象类型
	 * @return 返回实体对象列表
	 */
	public <T> List<T> queryAll(Class<T> classType) throws Exception{
		String sql = SELECT_TB_PREFIX + this.toLowerCaseFirstOne(classType.getSimpleName());
		this.showSQL(sql);
		return dbconn.execQuery(sql, new RowMapper<T>(classType));
	}
	
	 /**
     * 查询指定对象类型的对象信息，并按照指定的排序字段进行升序或降序排序
     * @param classType 对象类型
     * @param orderColumn 排序字段
     * @param ascOrDesc 降序或升序:asc表示升序，desc表示降序
     * @return 返回实体对象列表
     */
	public <T> List<T> queryAllWithOrder(Class<T> classType, String orderColumn, 
			String ascOrDesc) throws Exception{
		StringBuffer sqlBuffer = new StringBuffer();
		sqlBuffer.append(SELECT_TB_PREFIX);
		sqlBuffer.append(this.toLowerCaseFirstOne(classType.getSimpleName()));
		sqlBuffer.append(" order by ");
		sqlBuffer.append(orderColumn);
		sqlBuffer.append(" ");
		sqlBuffer.append(ascOrDesc);
		this.showSQL(sqlBuffer.toString());
		return dbconn.execQuery(sqlBuffer.toString(), new RowMapper<T>(classType));
	}
	
	/**
     * 查询指定SQL语句的对象信息列表
     * @param sql 查询语句
     * @param classType 对象类型
     * @param params SQL语句参数
     * @return 返回实体对象列表
     */
	public <T> List<T> query(String sql, Class<T> classType, Object... params) 
			  throws Exception{
		this.showSQL(sql);
		return dbconn.execQuery(sql, new RowMapper<T>(classType), params);
	}
	
	/**
     * 查询指定SQL语句的对象信息列表
     * @param sql 查询语句
     * @param classType 对象类型
     * @param params SQL语句参数
     * @return 返回实体对象列表
     */
	public <T> T queryForObj(String sql, Class<T> classType, Object... params) 
			throws Exception{
		this.showSQL(sql);
		return dbconn.execQuery(sql, new RowMapper<T>(classType), params).get(0);
	}
	
    /**
     * 分页查询实体对象列表信息
     * @param sql 原始的SQL语句
     * @param classType 对象类型
     * @param curPage 当前页码
     * @param rowsPerPage 每页显示的记录数
     * @param params SQL语句参数
     * @return 返回当前页码的分页对象
     */
	public <T> PageBean<T> queryByPage(String sql, Class<T> classType, int curPage, 
			int rowsPerPage, Object... params) throws Exception{
		 // 获取记录总数
		 int totalRows = this.getTotalRows(sql, params);
		 PageBean<T> pageBean = new PageBean<T>();
		 pageBean.setCurPage(curPage); // 设置当前页码
		 pageBean.initPageBean(totalRows, rowsPerPage); // 初始化分页对象的相关属性
		 // 生成当前分页查询语句（MySql）
		 String pageSql = pageBean.getPageMySQL(sql, curPage, rowsPerPage);
		 this.showSQL(pageSql);
		 // 执行查询操作
		 pageBean.setPageList(dbconn.execQuery(sql, new RowMapper<T>(classType), params));
		 return pageBean;
	}
	
	 /**
     * 保存对象到数据库中，若数据库中的用户表有自增序列，则需要指出表中自增列的字段名称，另外，
	 * 数据库中相应的自增序列的名称需按如下格式取名：class名称_自增列字段名称_SEQ,
	 * 例如用户的class为Users，自增序列字段名称为id，则数据库中的自增序列的名称取名为USERS_ID_SEQ.
     * @param obj 实体对象
     * @param sequenceKeyColumn 数据表自增序列的字段名称，若不存在，则置为null。
     * @return 返回被更新的记录数
	 * @throws IllegalAccessException 
	 * @throws IllegalArgumentException 
     */
	public <T> int insert(T obj, String sequenceKeyColumn) throws Exception{
		String tableName = TB_PREFIX + this.toLowerCaseFirstOne(obj.getClass().getSimpleName());
		// 自动生成对象的无自增序列插入SQL语句及其相关插入的参数值和类型
		Map<String, Object> sqlMap = SQLUtil.generateInsertWithNull(obj, tableName, sequenceKeyColumn);
		String sql = sqlMap.get("sql").toString(); // SQL语句
		Object[] paramsValues = (Object[])sqlMap.get("paramsValues"); // SQL语句的参数值
		// int[] paramsTypes = this.parseArrayToInt((Object[])sqlMap.get("paramsTypes")); // 参数值类型
		this.showSQL(sql);
		return dbconn.execUpdate(sql, paramsValues);
	}
	
    /**
     * 更新对象
     * @param obj 待更新的实体对象
     * @param keyColumn 更新对象的限定条件字段名称
     * @return 返回被更新的记录数
     */
	public <T> int update(T obj, String keyColumn) throws Exception{
		String tableName = TB_PREFIX + this.toLowerCaseFirstOne(obj.getClass().getSimpleName());
		// 自动生成对象的更新操作的SQL语句及其参数值
		Object[] updateSql = SQLUtil.generateUpdate(obj, tableName, keyColumn);
		this.showSQL(updateSql[0].toString());
		return dbconn.execUpdate(updateSql[0].toString(), (Object[])updateSql[1]);
	}
	
	/**
     * 删除对象
     * @param obj 待更新的实体对象
     * @param keyColumn 删除的限定条件字段
     * @return 返回被删除的记录数
     */
	public <T> int delete(T obj, String keyColumn) throws Exception{
		// 获取限定条件的值
		Object keyValue = ReflectionUtil.getFieldValue(obj, keyColumn);
		if(keyValue == null){
			throw new RuntimeException("["+obj.getClass()+"]中不存在属性'"+keyColumn+"'或属性值为空.");
		}
		StringBuffer sqlBuffer = new StringBuffer();
		sqlBuffer.append("delete from ");
		sqlBuffer.append(TB_PREFIX);
		sqlBuffer.append(this.toLowerCaseFirstOne(obj.getClass().getSimpleName()));
		sqlBuffer.append(" where ");
		sqlBuffer.append(keyColumn);
		sqlBuffer.append(" = ? ");
		this.showSQL(sqlBuffer.toString());
		return dbconn.execUpdate(sqlBuffer.toString(), keyValue.toString());
	}
	
	/**
     * 删除指定编号的实体对象
     * @param classType 对象类型
     * @param keyColumn ID对应的数据表列名称
     * @param id 实体对象编号
     * @return 返回删除的记录数
     */
	public <T> int deleteById(Class<T> classType, String keyColumn, Serializable id) 
			throws Exception{
		StringBuffer sqlBuffer = new StringBuffer();
		sqlBuffer.append("delete from ");
		sqlBuffer.append(TB_PREFIX);
		sqlBuffer.append(this.toLowerCaseFirstOne(classType.getSimpleName()));
		sqlBuffer.append(" where ");
		sqlBuffer.append(keyColumn);
		sqlBuffer.append(" = ? ");
		this.showSQL(sqlBuffer.toString());
		return dbconn.execUpdate(sqlBuffer.toString(), id);
	}
	
	 /**
     * 批量删除指定编号的实体对象
     * @param classType 对象类型
     * @param idColumnName 编号字段名称 
     * @param ids 待删除对象的编号数组
     */
	public <T> int deleteByIds(Class<T> classType, String idColumnName,
			Serializable[] ids) throws Exception{
		StringBuffer sqlBuffer = new StringBuffer();
		sqlBuffer.append("delete from ");
		sqlBuffer.append(TB_PREFIX);
		sqlBuffer.append(this.toLowerCaseFirstOne(classType.getSimpleName()));
		sqlBuffer.append(" where ");
		sqlBuffer.append(idColumnName);
		sqlBuffer.append(" = ? ");
		this.showSQL(sqlBuffer.toString());
		int rowNums = 0; // 删除的记录数
		for(int i = 0; i < ids.length; i++){
			rowNums += this.deleteById(classType, idColumnName, ids[i]);
		}
		return rowNums;
	}
	
    /**
     * 更新操作
     * @param sql 删除操作的SQL语句
     * @param params SQL语句中的参数值
     * @return 返回删除的记录数
     */
	public int update(String sql, Object... params) throws Exception{
		this.showSQL(sql);
		return dbconn.execUpdate(sql, params);
	}
	
	/**
     * 批量更新对象
     * @param objs 待更新的实体对象列表
     * @param keyColumn 主键字段名称
     */
	public <T> int batchUpdate(List<T> objs, String keyColumn) throws Exception{
		if(objs == null || objs.isEmpty()){
			return 0;
		}
		int updateNum = 0;
		// 自动生成对象的更新操作的SQL语句及其参数值
		for(int i = 0; i < objs.size(); i++){
			T obj = objs.get(i);
			updateNum += this.update(obj, keyColumn);
		}
		return updateNum;
	}
	
	 /**
     * 批量插入对象
     * @param objs 待新增的实体对象列表
     * @param keyColumn 数据表主键列名称
	 * @throws IllegalAccessException 
	 * @throws IllegalArgumentException 
     */
	public <T> int batchInsert(List<T> objs, String keyColumn) throws Exception {
		if(objs == null || objs.isEmpty()){
			return 0;
		}
		int updateNum = 0;
		// 自动生成对象的更新操作的SQL语句及其参数值
		for(int i = 0; i < objs.size(); i++){
			T obj = objs.get(i);
			updateNum += this.insert(obj, keyColumn);
		}
		return updateNum;
	}
	
	/**
	 * 统计指定统计数量的SQL语句
	 * @param sql 待执行的SQL语句
	 * @param params SQL语句参数值
	 * @return 返回记录总数
	 * @throws Exception
	 */
	public int getTotalRows(String sql, Object... params) throws Exception{
		String totalRowsSql = "select count(*) totalRows from ( "+sql+" )";
		this.showSQL(totalRowsSql);
		ResultSet rs = dbconn.queryForResultSet(totalRowsSql, params);
		while(rs.next()){
			return rs.getInt("totalRows");
		}
		return 0;
	}
	
    /**
     * 删除操作
     * @param sql 删除操作的SQL语句
     * @param params SQL语句中的参数值
     * @return 返回删除的记录数
     */
	public int delete(String sql, Object... params) throws Exception{
		this.showSQL(sql);
		return dbconn.execUpdate(sql, params);
	}
	
    /**
     * 获取下一个指定自增序列的值(MySql)
     * @param classType 对象类型
     * @param seqColName 自增字段名称
     * @return 返回指定表的主键自增序列的最新值
     */
	public <T> int getNextAutoIncrementVal(Class<T> classType, String seqColName) throws Exception{
		StringBuffer sqlBuf = new StringBuffer();
		sqlBuf.append("select max(");
		sqlBuf.append(seqColName);
		sqlBuf.append(")+1 nextId from ");
		sqlBuf.append(TB_PREFIX);
		sqlBuf.append(this.toLowerCaseFirstOne(classType.getSimpleName()));
		this.showSQL(sqlBuf.toString());
	    ResultSet rs = dbconn.queryForResultSet(sqlBuf.toString());
	    if(rs.next()){
	    	return rs.getInt("nextId");
	    }else{
	    	return 0;
	    }
	}	

	/**
	 * 首字母转小写
	 * @param str 待转换的字符创
	 * @return 返回首字母小写后的字符串
	 */
	public String toLowerCaseFirstOne(String str){
	     if(Character.isLowerCase(str.charAt(0))){
	          return str;
	     }else{
	         return (new StringBuilder()).append(Character.toLowerCase(str.charAt(0))).
	        		 append(str.substring(1)).toString();
	     }
	}
	
	/**
	 * 首字母转大写
	 * @param str 待转换的字符串
	 * @return 返回首字母大写的字符串
	 */
	public String toUpperCaseFirstOne(String str){
	     if(Character.isUpperCase(str.charAt(0))){
	         return str;
	     }else{
	         return (new StringBuilder()).append(Character.toUpperCase(str.charAt(0))).
	        		 append(str.substring(1)).toString();
	     }
	}

    /**
     * 打印SQL语句
     * @param sql
     */
    public void showSQL(String sql){
    	System.out.println(sql);
    }
    
}