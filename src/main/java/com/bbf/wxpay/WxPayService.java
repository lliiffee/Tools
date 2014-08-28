package com.bbf.wxpay;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCallback;


public class WxPayService {
 
	 private DataSource dataSource;
     /**
      * spring提供的jdbc操作辅助类
 */
     private JdbcTemplate jdbcTemplate;
 
     // 设置数据源
     public void setDataSource(DataSource dataSource) {
         this.jdbcTemplate = new JdbcTemplate(dataSource);
     }
     
 
   
   public List<WxPayNotify> getHandleList()
   {
	   java.util.Date current=new java.util.Date();
       java.text.SimpleDateFormat sdf=new java.text.SimpleDateFormat("yyyyMMdd"); 
       String c=sdf.format(current)+"000000";
       System.out.println(c);
		List<WxPayNotify> list = jdbcTemplate.query(
		"select * from tb_pay_weixin_notify where handle_status=? and time_end <? limit 0,3000"
		,new Object[]{1,c}
		,new int[]{java.sql.Types.INTEGER,java.sql.Types.VARCHAR}
		, new WxNotifyMapper());
        return list;
   }
   
   
   public int[] batchUpdateByTranId(final List<WxPayNotify> beans) {

       
       
       int[] updateCounts = (int[]) this.jdbcTemplate.execute(" update tb_pay_weixin_notify set handle_status = ?  where transaction_id = ? ",
       		new PreparedStatementCallback(){   
           public Object doInPreparedStatement(PreparedStatement ps)throws SQLException, DataAccessException {   
               int length = beans.size();   
               ps.getConnection().setAutoCommit(false);   
               for(int i=0;i<length;i++){   
                  ps.setInt(1, 0);
                   ps.setString(2, beans.get(i).getTransactionId());
                   ps.addBatch();   
               }   
               Object o = ps.executeBatch();   
               ps.getConnection().commit();   
               ps.getConnection().setAutoCommit(true);   
                            //如果用<aop:config>  来控制事务，需要把上一行注掉，否则会报错   
               return  o;   
           }});  
       
       return updateCounts;
   }
   
  
   public List<WxPayFeedBack> getComplaintList()
   {
		List<WxPayFeedBack> list = jdbcTemplate.query(
		"select * from tb_pay_weixin_feedback where handle_status=? "
		,new Object[]{1}
		,new int[]{java.sql.Types.INTEGER}
		, new WxFeedBackMapper());
        return list;
   }
   
public int[] batchUpdateCompaintByFeedBackId(final List<WxPayFeedBack> beans) {

       
       
       int[] updateCounts = (int[]) this.jdbcTemplate.execute(" update tb_pay_weixin_feedback set handle_status = ?  where feed_back_id = ? ",
       		new PreparedStatementCallback(){   
           public Object doInPreparedStatement(PreparedStatement ps)throws SQLException, DataAccessException {   
               int length = beans.size();   
               ps.getConnection().setAutoCommit(false);   
               for(int i=0;i<length;i++){   
                  ps.setInt(1, 0);
                   ps.setString(2, beans.get(i).getFeedBackId());
                   ps.addBatch();   
               }   
               Object o = ps.executeBatch();   
               ps.getConnection().commit();   
               ps.getConnection().setAutoCommit(true);   
                            //如果用<aop:config>  来控制事务，需要把上一行注掉，否则会报错   
               return  o;   
           }});  
       
       return updateCounts;
   }
	
}
