package com.bbf.sms;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;

import com.fung.xml.XMLUtil;

public class MsgCenter {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
/*
		 String PostData;
		try {
			
 
			{    
			  PostData = "sname=dlbbfxx0&spwd=H4b6qGjG&scorpid=&sprdid=1012818&sdst=13544461206&smsg="+java.net.URLEncoder.encode("你的手机注册验证码：381551 【八百方】" ,"utf-8");
		//	PostData = "sname=dlbbfxx0&spwd=H4b6qGjG&scorpid=&sprdid=1012808&sdst=13544461206&smsg="+java.net.URLEncoder.encode("testtest 【八百方】","utf-8");
			 //out.println(PostData);
	         String ret =SMS(PostData, "http://cf.lmobile.cn/submitdata/Service.asmx/g_Submit");
	         System.out.println(ret);
	         Map map= XMLUtil.doXMLParse(ret);
				if(map.get("Reserve").equals("0")){
					System.out.println("true");
				}else
				{ 
					System.out.println("false");
				}
	         
	         //请自己反序列化返回的字符串并实现自己的逻辑
			}
		
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
  */
		
		singleInfoQuery();
	//	sm_GetRemain();
		
	}
	
	
	public static void singleInfoQuery()
	{
		 String PostData;
			try {
				
				/*
				 * 	    <tag name="sendMsgName" value="dlbbfxx0"/>
			<tag name="sendMsgPwd" value="H4b6qGjG"/>
			<tag name="sendMsgChanel" value="1012818"/>
			<tag name="sendMsgChanel_notify" value="1012808"/>
			<tag name="sendMsgChanel_promo" value="1012812"/>
				 */
				//for(int i=0 ;i<= 1 ;i++)
				{    
				  PostData ="accountId=dlbbfxx0&accuntPassWord=H4b6qGjG&productextendId=1012818&mobilePhone=13544461206&startTime=2015-02-09&endTime=2015-02-11&pageSize=10&curPage=0";
			//	PostData = "sname=dlbbfxx0&spwd=H4b6qGjG&scorpid=&sprdid=1012808&sdst=13544461206&smsg="+java.net.URLEncoder.encode("testtest 【八百方】","utf-8");
				 //out.println(PostData);
		         String ret =SMS(PostData, "http://cf.lmobile.cn/submitdata/Service.asmx/SingleInfoQuery");
		         System.out.println(ret);
//		         Map map= XMLUtil.doXMLParse(ret);
//					if(map.get("Reserve").equals("0")){
//						System.out.println("true");
//					}else
//					{ 
//						System.out.println("false");
//					}
		         
		         //请自己反序列化返回的字符串并实现自己的逻辑
				}
			
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	}
	
	 
	public static void sm_GetRemain()
	{
		 String PostData;
			try {
				
			 
				{    
				  PostData ="sname=dlbbfxx0&spwd=H4b6qGjG&sprdid=1012818&scorpid=";
		 
		         String ret =SMS(PostData, "http://cf.lmobile.cn/submitdata/Service.asmx/Sm_GetRemain");
		         System.out.println(ret);
 
				}
			
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	}
	
	
	
	
	 public static String SMS(String postData, String postUrl) {
	        try {
	            //发送POST请求
	            URL url = new URL(postUrl);
	            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
	            conn.setRequestMethod("POST");
	            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
	            conn.setRequestProperty("Connection", "Keep-Alive");
	            conn.setUseCaches(false);
	            conn.setDoOutput(true);

	            conn.setRequestProperty("Content-Length", "" + postData.length());
	            OutputStreamWriter out = new OutputStreamWriter(conn.getOutputStream(), "UTF-8");
	            out.write(postData);
	            out.flush();
	            out.close();

	            //获取响应状态
	            if (conn.getResponseCode() != HttpURLConnection.HTTP_OK) {
	                System.out.println("connect failed!");
	                return "";
	            }
	            //获取响应内容体
	            String line, result = "";
	            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream(), "utf-8"));
	            while ((line = in.readLine()) != null) {
	                result += line + "\n";
	            }
	            in.close();
	            return result;
	        } catch (IOException e) {
	            e.printStackTrace(System.out);
	        }
	        return "";
	    }

}
