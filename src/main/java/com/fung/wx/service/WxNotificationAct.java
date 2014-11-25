package com.fung.wx.service;

import com.fung.wx.util.TokenUtil;
import com.fung.wx.util.WeiXinUtil;

public class WxNotificationAct {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		 
		
		sendMsg();
			
			
			
		}


	private static void sendMsg()
	{
		String json ="{"
				 +"   \"touser\":\"TOUSER\","
				 +"   \"msgtype\":\"text\","
				   +"  \"text\":"
				  +"   {"
				  +"   \"content\":\"CCQ_MSG\""
				         +"   }"
				         +"}";
		 
		TokenUtil tk=new TokenUtil();
	//	String key="wxbf261f64e52a3ceb";
	//	String sec="6ee4bd7cb3c20c5c53a1015f3fc37edf";
		
	//	String key="wx7179cc98fb47eff5";
	//	String sec="823ae7209cb695d4a4a0c71071d6006e";
		String key = "wx3d2a1aed54bca0e5";
		String sec = "ba3b4085713a09ea86f52f5b7d8e7b9e";
		
		String fung="otwmFuFWazuAWWP1HrFhJJ3yagIg";
		String freeman="otwmFuAEiplEh22PryPkTfplIaJw";
		//String fung="oTNesjrqNt4ZIZ8zRe2-WVnt98M0";
		//o69Ras_or-q5yt03qtFNWUhzCWHI
		//o69Ras_or-q5yt03qtFNWUhzCWHI
		System.out.println(tk.getToken(key,sec));
		String result="test2";
		//String psUrl="https://api.weixin.qq.com/cgi-bin/message/custom/send?access_token=ACCESS_TOKEN";
		//WeiXinUtil.httpsRequest(psUrl.replace("ACCESS_TOKEN",tk.getToken(key,sec) ) ,"POST", json.replace("TOUSER", fung).replace("CCQ_MSG", result));
	}
	
	
	 
	 
}
