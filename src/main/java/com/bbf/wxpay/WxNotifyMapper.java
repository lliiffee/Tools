package com.bbf.wxpay;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

import org.springframework.jdbc.core.RowMapper;


public class WxNotifyMapper  implements RowMapper{

	@Override
	public Object mapRow(ResultSet set, int index) throws SQLException {
		 
		WxPayNotify log=new WxPayNotify();
		 log.setId(set.getInt("id"));
		 log.setAppId(set.getString("app_id"));
		 log.setHandleStatus(set.getInt("handle_status") );
		 log.setNotifyId(set.getString("notify_id"));
		 log.setOpenId(set.getString("open_id"));
		 log.setOutTradeNo(set.getString("out_trade_no"));
		 log.setTimeEnd(set.getString("time_end"));
		 log.setTradeState(set.getInt("trade_state"));
		 log.setTransactionId(set.getString("transaction_id"));
 
		return log;
		/*
		 * public Object mapRow(ResultSet set, int index) throws SQLException {
          Person person = new Person(set.getInt("id"), set.getString("name"), set
                  .getInt("age"), set.getString("sex"));
         return person;
      }
		 */
	}

}