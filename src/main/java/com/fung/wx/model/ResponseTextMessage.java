package com.fung.wx.model;

 
public class ResponseTextMessage extends ResponseBaseMessage {
	
	//回复的消息内容
	private String Content;

	public String getContent() {
		return Content;
	}

	public void setContent(String content) {
		Content = content;
	}

}
