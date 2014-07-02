package com.fung.db.basic;

import java.sql.Connection;
import java.sql.DriverManager;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

public class ConnectionConcretFactory {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}
	/*
	 * oracle.jdbc.driver.OracleDriver
	 * jdbc:oracle:thin:@localhost:1521:orcl
	 * user
	 * passowrd
	 */
	public Connection getConByBaseMethod(String className,String url,String user,String pwd) throws Exception
	{
		Connection conn=null;
		/*
		try {
			Class.forName(className).newInstance();
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    
	    try {
			 conn= DriverManager.getConnection(url,user,pwd);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    */
		Class.forName(className).newInstance();
		 conn= DriverManager.getConnection(url,user,pwd);
	    return conn;
	    
	}
	/*
	 * jdbc/webtest
	 */
	public Connection getConByJndi(String poolName) throws Exception
	{
		 Context initCtx = new InitialContext();
	     Context envCtx = (Context)initCtx.lookup("java:comp/env");
	     DataSource ds = (DataSource)envCtx.lookup(poolName);
	    Connection conn = ds.getConnection();
	    
	    return conn;
	}

}
