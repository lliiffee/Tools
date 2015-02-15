package com.fung.jdbc.common;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

 

/**
 * 定义数据库连接辅助类DBConn
 * @author fung
 * @version 1.0 
 */
public class DBConn {
    private Connection conn = null;
    private PreparedStatement pstmt = null;
    private ResultSet rs = null; 
    
    /**
     * 创建数据库的连接
     * @return 返回数据库连接对象
     */
    public Connection getConn(){        
        try {                     
			// 加载数据库驱动
            Class.forName(JdbcConfig.DRIVERCLASSNAME);
            // 创建Connection接口对象，用于获取MySQL数据库的连接对象
            conn = DriverManager.getConnection(JdbcConfig.URL, JdbcConfig.USERNAME, JdbcConfig.PASSWORD);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }    
        return conn;
    }

	/**
	 * 更新数据库操作（包括增删改操作）
	 * @param sql  待执行sql语句
	 * @param objs 用于设置预编译语句中带入的参数
	 * @return null
	 * @throws Exception 
	 */
	public int execUpdate(String sql, Object ...objs) throws Exception{
			// 获取预编译环境
			pstmt = this.getConn().prepareStatement(sql);
			if(objs != null && objs.length > 0){
				for(int i = 0; i < objs.length; i++){
					pstmt.setObject(i+1, objs[i]);
				}
			}
			// 执行更新语句
			int result = pstmt.executeUpdate();
			// 断开连接，释放资源
			this.close(rs, pstmt, conn);
			return result;
	}

	/**
	 * 数据库查询操作
	 * @param sql 待执行sql语句
	 * @param objs 用于设置预编译语句中带入的参数
	 * @return 类T的List数据类型，即返回查询到的所有数据信息
	 * @throws Exception 
	 */
	public <T> List<T> execQuery(String sql, RowMapper<T> mapper, Object ...objs) throws Exception{
		// 获取预编译环境
		pstmt = this.getConn().prepareStatement(sql);
		if(objs != null && objs.length > 0){
			for(int i = 0; i < objs.length; i++){
				pstmt.setObject(i+1, objs[i]);
			}
		}
		// 执行更新语句
		rs = pstmt.executeQuery();
		// 执行关系到对象的映射
		List<T> result = mapper.mapRows(rs);
		// 断开连接，释放资源
		this.close(rs, pstmt, conn);
		return result;
	}	
	
	/**
	 * 执行执行的SQL语句并返回结果集
	 * @param sql SQL语句
	 * @param params 参数
	 * @return 返回结果集
	 */
	public ResultSet queryForResultSet(String sql, Object... objs) throws Exception{
		// 获取预编译环境
		pstmt = conn.prepareStatement(sql);
		if(objs != null && objs.length > 0){
			for(int i = 0; i < objs.length; i++){
				pstmt.setObject(i+1, objs[i]);
			}
		}
		// 执行更新语句
		rs = pstmt.executeQuery();
		// 断开连接，释放资源
		this.close(null, pstmt, conn);
		return rs;
	}
	
	/**
	 * 关闭数据库连接，释放所占的系统资源
	 * @param rs结果集、ppst预编译命令、conn数据库
	 * @return null
	 * @throws Exception 
	 */
	public void close(ResultSet rs, PreparedStatement ppst, Connection conn) throws Exception{
		if(rs != null){
			rs.close();
		}
		if(ppst != null){ 
			ppst.close();
		}
		if(conn != null){
			conn.close();
		}
	}
}
