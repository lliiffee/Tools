package com.wx.coupon.model;

public class WxCardCash extends   WxCard{

	 public   WxCardCash(){
		 init("CASH");
	 }
	 
 
	public void setLeastCost(int leastCost) {//代金券专用，表示起用金额。（单位为分）
		 m_data.put("least_cost", leastCost);
	}
	 
	public void setReduceCost(int reduceCost) {//代金券专用，表示减免金额。（单位为分）
		 m_data.put("reduce_cost", reduceCost);
	}

	public int getLeastCost() {
		return m_data.getIntValue("least_cost");
	}

	public int getReduceCost() {
		return m_data.getIntValue("reduce_cost");
	}
	 
	
	 
}
