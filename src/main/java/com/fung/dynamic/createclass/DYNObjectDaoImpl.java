package com.fung.dynamic.createclass;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fung.db.basic.DBUtil;

public class DYNObjectDaoImpl implements DYNObjectDao{

	 @Override
	    public List<Object> getObjectList(Connection conn, String tableName)
	    {
	        Map<String, Class> columnMap = new HashMap<String, Class>();
	        DYNCglibBeanUtil beanUtil = null;
	        String sql = "select * from " + tableName;
	        Object ob = null;
	        try
	        {
	            ResultSetMetaData rs = conn.prepareStatement(sql).executeQuery().getMetaData();
	            for(int i = 1; i <= rs.getColumnCount(); i++)
	            {
	                columnMap.put(rs.getColumnName(i), Class.forName(rs.getColumnClassName(i)));
	            }
	            ob = new DYNCglibBeanUtil(columnMap).getObject();
	        } catch (SQLException e)
	        {
	            e.printStackTrace();
	        } catch (ClassNotFoundException e)
	        {
	            e.printStackTrace();
	        }
	        return this.getObjectList(conn, ob, tableName);
	    }
	    
	    public List<Object> getObjectList(Connection conn, Object ob, String tableName)
	    {
	        String sql = "select * from " + tableName;
	        ResultSet rs = null;
	        List<Object> list = null;
	        try
	        {
	            rs = conn.prepareStatement(sql).executeQuery();
	            list = DYNGenerateUtil.generateObjectListFromDB(ob.getClass(), rs);
	        } catch (SQLException e)
	        {
	            e.printStackTrace();
	        } finally
	        {
	            DBUtil.closeConnection(conn);
	        }
	        return list;
	    }
}
