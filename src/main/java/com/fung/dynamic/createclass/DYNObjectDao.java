package com.fung.dynamic.createclass;

import java.sql.Connection;
import java.util.List;

public interface DYNObjectDao {
	List getObjectList(Connection conn, String tableName);//通过表名和前台传的连接返回对象
	
	List getObjectList(Connection conn, Object ob, String tableName); //和上面的方法一样 只是剥离开重新调用了下

}
