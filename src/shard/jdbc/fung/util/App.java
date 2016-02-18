package fung.util;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.sql.DataSource;

import com.dangdang.ddframe.rdb.sharding.api.ShardingDataSource;
import com.dangdang.ddframe.rdb.sharding.api.ShardingValue;
import com.dangdang.ddframe.rdb.sharding.api.rule.BindingTableRule;
import com.dangdang.ddframe.rdb.sharding.api.rule.DataSourceRule;
import com.dangdang.ddframe.rdb.sharding.api.rule.ShardingRule;
import com.dangdang.ddframe.rdb.sharding.api.rule.TableRule;
import com.dangdang.ddframe.rdb.sharding.api.strategy.database.DatabaseShardingStrategy;
import com.dangdang.ddframe.rdb.sharding.api.strategy.database.SingleKeyDatabaseShardingAlgorithm;
import com.dangdang.ddframe.rdb.sharding.api.strategy.table.MultipleKeysTableShardingAlgorithm;
import com.dangdang.ddframe.rdb.sharding.api.strategy.table.TableShardingStrategy;
import com.google.common.base.Joiner;
import com.google.common.collect.Range;
import com.google.common.collect.Sets;

public class App {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// 首先我们来构造DataSourceRule对象，它是来描述数据源的分布规则的。
		Map<String, DataSource> dataSourceMap = new HashMap<>(2);
		dataSourceMap.put("ds_0", DBUtil.createDataSource("test"));
		dataSourceMap.put("ds_1", DBUtil.createDataSource("test2"));
		
		 DataSourceRule dataSourceRule = new DataSourceRule(dataSourceMap);
		
		 
		// 全局默认策略与特定表策略
		// 策略是作用在特定的表规则上的，数据源策略与表策略与特定表相关

		//使用了默认策略配置
		  TableRule orderTableRule = new TableRule("t_order", Arrays.asList("t_order_0", "t_order_1"), dataSourceRule);
		  TableRule orderItemTableRule = new TableRule("t_order_item", Arrays.asList("t_order_item_0", "t_order_item_1"), dataSourceRule);
		  ShardingRule shardingRule = new ShardingRule(dataSourceRule, Arrays.asList(orderTableRule, orderItemTableRule),
		                Arrays.asList(new BindingTableRule(Arrays.asList(orderTableRule, orderItemTableRule))),
		                new DatabaseShardingStrategy("user_id", new App.ModuloDatabaseShardingAlgorithm()),
		                new TableShardingStrategy(Arrays.asList("order_id"), new App.MultipleKeysModuloTableShardingAlgorithm()));
		

		String sql = "SELECT i.* FROM t_order o JOIN t_order_item i ON o.order_id=i.order_id WHERE o.user_id=? AND o.order_id=?";
		  
		//	String sql = "SELECT * FROM t_order o  where o.order_id=?";
			
		ShardingDataSource dataSource=new ShardingDataSource(shardingRule);
         
        		
                Connection conn;
				try {
					conn = dataSource.getConnection();
					   PreparedStatement pstmt = conn.prepareStatement(sql);
		               
			            pstmt.setInt(1, 10);
			            pstmt.setInt(2, 1001);
			            ResultSet rs = pstmt.executeQuery();
			            while (rs.next()) {
			                System.out.println(rs.getInt(1));
			                System.out.println(rs.getInt(2));
			                System.out.println(rs.getInt(3));
			            }
			            rs.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
             
         
        
	}
	
	
	 public static final class ModuloDatabaseShardingAlgorithm implements SingleKeyDatabaseShardingAlgorithm<Integer> {
		    
		    /**
		    *  select * from t_order from t_order where order_id = 11 
		    *          └── SELECT *  FROM t_order_1 WHERE order_id = 11
		    *  select * from t_order from t_order where order_id = 44
		    *          └── SELECT *  FROM t_order_0 WHERE order_id = 44
		    */
		    public String doEqualSharding(final Collection<String> tableNames, final ShardingValue<Integer> shardingValue) {
		        for (String each : tableNames) {
		            if (each.endsWith(shardingValue.getValue() % 2 + "")) {
		                return each;
		            }
		        }
		        throw new IllegalArgumentException();
		    }
		    
		    /**
		    *  select * from t_order from t_order where order_id in (11,44)  
		    *          ├── SELECT *  FROM t_order_0 WHERE order_id IN (11,44) 
		    *          └── SELECT *  FROM t_order_1 WHERE order_id IN (11,44) 
		    *  select * from t_order from t_order where order_id in (11,13,15)  
		    *          └── SELECT *  FROM t_order_1 WHERE order_id IN (11,13,15)  
		    *  select * from t_order from t_order where order_id in (22,24,26)  
		    *          └──SELECT *  FROM t_order_0 WHERE order_id IN (22,24,26) 
		    */
		    public Collection<String> doInSharding(final Collection<String> tableNames, final ShardingValue<Integer> shardingValue) {
		        Collection<String> result = new LinkedHashSet<>(tableNames.size());
		        for (Integer value : shardingValue.getValues()) {
		            for (String dataSourceName : tableNames) {
		                if (dataSourceName.endsWith(value % 2 + "")) {
		                    result.add(dataSourceName);
		                }
		            }
		        }
		        return result;
		    }
		    
		    /**
		    *  select * from t_order from t_order where order_id between 10 and 20 
		    *          ├── SELECT *  FROM t_order_0 WHERE order_id BETWEEN 10 AND 20 
		    *          └── SELECT *  FROM t_order_1 WHERE order_id BETWEEN 10 AND 20 
		    */
		    public Collection<String> doBetweenSharding(final Collection<String> tableNames, final ShardingValue<Integer> shardingValue) {
		        Collection<String> result = new LinkedHashSet<>(tableNames.size());
		        Range<Integer> range = (Range<Integer>) shardingValue.getValueRange();
		        for (Integer i = range.lowerEndpoint(); i <= range.upperEndpoint(); i++) {
		            for (String each : tableNames) {
		                if (each.endsWith(i % 2 + "")) {
		                    result.add(each);
		                }
		            }
		        }
		        return result;
		    }
		}
	 
	 
	 
	 public static final class MultipleKeysModuloTableShardingAlgorithm implements MultipleKeysTableShardingAlgorithm {
		    
		    @Override
		    public Collection<String> doSharding(final Collection<String> availableTargetNames, final Collection<ShardingValue<?>> shardingValues) {
		        Set<Integer> orderIdValueSet = getShardingValue(shardingValues, "order_id");
		        Set<Integer> userIdValueSet = getShardingValue(shardingValues, "user_id");
		    
		        List<String> result = new ArrayList<>();
		        /*
		        userIdValueSet[10,11] + orderIdValueSet[101,102] => valueResult[[10,101],[10,102],[11,101],[11,102]]
		         */
		        Set<List<Integer>> valueResult = Sets.cartesianProduct(userIdValueSet, orderIdValueSet);
		        for (List<Integer> value : valueResult) {
		            String suffix = Joiner.on("").join(value.get(0) % 2, value.get(1) % 2);
		            for (String tableName : availableTargetNames) {
		                if (tableName.endsWith(suffix)) {
		                    result.add(tableName);
		                }
		            }
		        
		        }
		        return result;
		    }
		    
		    private Set<Integer> getShardingValue(final Collection<ShardingValue<?>> shardingValues, final String shardingKey) {
		        Set<Integer> valueSet = new HashSet<>();
		        ShardingValue<Integer> shardingValue = null;
		        for (ShardingValue<?> each : shardingValues) {
		            if (each.getColumnName().equals(shardingKey)) {
		                shardingValue = (ShardingValue<Integer>) each;
		                break;
		            }
		        }
		        if (null == shardingValue) {
		            return valueSet;
		        }
		        switch (shardingValue.getType()) {
		            case SINGLE:
		                valueSet.add(shardingValue.getValue());
		                break;
		            case LIST:
		                valueSet.addAll(shardingValue.getValues());
		                break;
		            case RANGE:
		                for (Integer i = shardingValue.getValueRange().lowerEndpoint(); i <= shardingValue.getValueRange().upperEndpoint(); i++) {
		                    valueSet.add(i);
		                }
		                break;
		            default:
		                throw new UnsupportedOperationException();
		        }
		        return valueSet;
		    }
		}

}
