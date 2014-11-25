package com.wx.coupon.model;
/**
 *
 * @author jackylian
 */
public class WxCardGeneralCoupon extends WxCard
{
    public WxCardGeneralCoupon()
    {
        init("GENERAL_COUPON");
    }
    
    public void setDefaultDetail(String detail)
    {
        m_data.put("default_detail", detail);
    }
    
    public String getDefaultDetail()
    {
        return m_data.getString("default_detail");
    }
}
