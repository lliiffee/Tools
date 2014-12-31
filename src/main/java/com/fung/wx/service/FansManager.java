package com.fung.wx.service;

import java.io.FileWriter;
import java.io.PrintWriter;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.fung.wx.model.WxFans;
import com.fung.wx.util.WeiXinUtil;

public class FansManager {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		 String token="pOyJWXVOixYa1s-GnAu-Dy_tVoYVt1THblSAUf5KsqSJFttmIej3wJN96bYDbsESpUmeHvC5MhJnLgm4hlLJ5AdfDz72ir2h_caxDCSJsw4";
		 String url="https://api.weixin.qq.com/cgi-bin/user/get?access_token="+token;
		 JSONObject jsonObject = WeiXinUtil.httpsRequest(url, "GET", "");
		 getData(jsonObject,"e:\\temp\\fans.txt",token);
	}
	
	//生产	
//	public final static String appid="wx7179cc98fb47eff5";
//	public final static String appsecret="823ae7209cb695d4a4a0c71071d6006e";
	
	private static void getData(JSONObject jsonObject,String path,String token)
	{
		 
		PrintWriter	 pw=null;
		 
		
		try {
			 
			pw = new PrintWriter(new FileWriter(path));
			JSONArray arr=	jsonObject.getJSONObject("data").getJSONArray("openid");
			  
	             for  (int i=0;i<arr.size();i++ ) { 
	            	 String requestUrl = "https://api.weixin.qq.com/cgi-bin/user/info?access_token="+token+"&openid="+arr.getString(i)+"&lang=zh_CN"; 
	            	 WxFans bean= WeiXinUtil.getWxFans(requestUrl);
	            	    pw.println("&&&"+arr.getString(i)
	            	    		+"&&&"+bean.getNickname()
	            	    		+"&&&"+ bean.getGender()
	            	    		+"&&&"+ ""  //language
	            	    		+"&&&"+ bean.getCity()
	            	    		+"&&&"+ bean.getProvince()
	            	    		+"&&&"+ bean.getCountry()
	            	    		+"&&&"+ bean.getHeadimgurl()
	            	    		+"&&&"+ bean.getSubscribeTime()
	            	    		+"&&&"+ bean.getSubscribe()
	            	    		+"&&&"+ bean.getUnionid()
	            	    		+"&&&"    //+ bean.getPrivilege() 
	            	    		);
	            	    pw.flush();
	            	  
	            	 }
	             
	                
			 
		} catch (Exception e) {
			e.printStackTrace();
		}finally
		{
			try {
				 
				pw.close();
				
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	

}
