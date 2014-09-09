package com.fung.wx.model;
/**
 * 请求图片消息
*    
 */
public class RequestImageMessage extends RequestBaseMessage {
	
	// 图片链接   
    private String PicUrl;

	public String getPicUrl() {
		return PicUrl;
	}

	public void setPicUrl(String picUrl) {
		PicUrl = picUrl;
	}  


}
