package com.fung.wx.model;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

public class MenuMapper  implements RowMapper{

	@Override
	public Object mapRow(ResultSet set, int index) throws SQLException {
		WxMenuSetting ws=new WxMenuSetting();
		
		ws.setId(set.getInt("id"));
		ws.setMenuName(set.getString("menu_name"));
		ws.setMenuType(set.getString("menu_type"));
		ws.setUrl(set.getString("url"));
		ws.setParent(set.getInt("parent"));
		
		return ws;
	}

}
