package com.bbf.wxpay;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

public class WxFeedBackMapper implements RowMapper{

	@Override
	public Object mapRow(ResultSet set, int index) throws SQLException {
		 
		WxPayFeedBack log=new WxPayFeedBack();
		  
		 log.setAppId(set.getString("app_id"));
		 log.setHandleStatus(set.getInt("handle_status") );
		 log.setOpenId(set.getString("open_id"));
		 log.setExtInfo(set.getString("ext_info"));
		 log.setFeedBackId(set.getString("feed_back_id"));
		 log.setHandleStatus(set.getInt("handle_status"));
		 log.setPicUrl(set.getString("pic_url"));
		 log.setReason(set.getString("reason"));
		 log.setRtTimeStamp(set.getString("rt_time_stamp"));
		 log.setSolution(set.getString("solution"));
		 log.setStatus(set.getInt("status"));
		 log.setTransId(set.getString("trans_id"));
		return log;
	}
}