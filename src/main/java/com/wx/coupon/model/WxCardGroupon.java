package com.wx.coupon.model;

/**
 *
 * @author jackylian
 */
public class WxCardGroupon extends WxCard
{

    public WxCardGroupon()
    {
        init("GROUPON");
    }
    
    public void setDealDetail(String detail)
    {
        m_data.put("deal_detail", detail);
    }
    
}
