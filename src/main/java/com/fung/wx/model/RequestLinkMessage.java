package com.fung.wx.model;

/**
 * 请求的链接信息
*     
*
 */
public class RequestLinkMessage extends RequestBaseMessage {
	
	//消息标题
	private String Title;
	  // 消息描述   
    private String Description;  
    // 消息链接   
    private String Url;
	public String getTitle() {
		return Title;
	}
	public void setTitle(String title) {
		Title = title;
	}
	public String getDescription() {
		return Description;
	}
	public void setDescription(String description) {
		Description = description;
	}
	public String getUrl() {
		return Url;
	}
	public void setUrl(String url) {
		Url = url;
	}  

}
