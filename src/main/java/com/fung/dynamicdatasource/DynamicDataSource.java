package com.fung.dynamicdatasource;

/**
 * 建立动态数据源
 * 
 * @author fung
 * 
 */
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

public class DynamicDataSource extends AbstractRoutingDataSource {

 protected Object determineCurrentLookupKey() {
  // 在进行DAO操作前，通过上下文环境变量，获得数据源的类型
	 System.out.println(DataSourceContextHolder.getDataSourceType());
  return DataSourceContextHolder.getDataSourceType();
 }

}
