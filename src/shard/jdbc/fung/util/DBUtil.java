package fung.util;

import javax.sql.DataSource;

import org.apache.commons.dbcp.BasicDataSource;

public class DBUtil {

	public static DataSource createDataSource(final String dataSourceName) {
	    BasicDataSource result = new BasicDataSource();
	    result.setDriverClassName(com.mysql.jdbc.Driver.class.getName());
	    result.setUrl(String.format("jdbc:mysql://localhost:3306/%s", dataSourceName));
	    result.setUsername("root");
	    result.setPassword("123456");
	    return result;
	}

}
