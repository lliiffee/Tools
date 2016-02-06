package com.fung.dynamic.createclass;

import java.sql.Connection;
import java.util.List;

import com.fung.db.basic.DBUtil;

public class DYNClient {
	public static void main(String[] args)
    {
		DBUtil dbutil=new DBUtil();
        Connection conn = dbutil.getConnection();
        
        DYNObjectDaoImpl odi = new DYNObjectDaoImpl();
        
        List<Object> list = odi.getObjectList(conn, "TP_CD00101");
        
        System.out.println(list.size());
        
    }
}


/*
 * 还有点是对事务的支持我没做，还有就是获得List后如何使用也没写，既然在daoImpl类中可以得到Class，
 * 那么再定义一个获取对于的Class的工具类，构造一个公共的方法获取List中对象的数据的方法即可，在这里我简单提下，
 * 方法参数就为(List list, Class class)，在其中运用反射即可获取所有数据
 */
